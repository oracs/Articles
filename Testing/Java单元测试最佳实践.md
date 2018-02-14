## Java单元测试最佳实践

**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.4.20 | 丁一 | 初稿 |

**目录**

[TOC]

### 实践一 如何mock静态方法
```java
源代码：
主体业务
public class BplTypeByCellAction extends AbstractLteMoFieldAction
{
    public BplTypeByCellAction(PageView view, FieldInfoObject field)
    {
        super(view, field);
    }

    @Override
    public void createValueChangeAction(PageView page, String sourceName, String sourceValue) throws RelationException
    {
        setBplVisibleAndEnable(page, sourceValue);
    }

    @Override
    public void editValueChangeAction(PageView page, String sourceName, String sourceValue) throws RelationException
    {
        setBplVisibleAndEnable(page, sourceValue);
    }

    private void setBplVisibleAndEnable(PageView page, String cellMoi) throws RelationException
    {
        String bplNum = ClntBoardUtils.getBplProductNumberByCellMoi(page, cellMoi);
        LteCmClntUtil.setVisibleByBPLType(page, bplNum, BplRules.VISIBLE_DEFINE.get(page.getMocName()));
        LteCmClntUtil.setEnableByBPLType(page, bplNum, BplRules.ENABLE_DEFINE.get(page.getMocName()));
    }
}
静态类
public class ClntBoardUtils
{
    private static DebugPrn debugLog = new DebugPrn(ClntBoardUtils.class.getName());

    public static String getBplProductNumberByCellMoi(PageView view, String cellMoi) throws RelationException
    {
    		逻辑较复杂，具体实现代码省略
    }
}
```
getBplProductNumberByCellMoi由于此静态方法实现比较复杂，需要MOCK掉此方法的具体行为，方便主体业务的测试，具体实现方法如下。
```java
@RunWith(PowerMockRunner.class)
@PrepareForTest(ClntBoardUtils.class)
public class BplTypeByCellActionTest
{
    static PageView mockPageView;
    static FakeMoField coperTypeFakeMoField;
    BplTypeByCellAction bplTypeByCellAction;

    @BeforeClass
    public static void init() throws RelationException
    {
        mockPageView = mock(PageView.class);
        when(mockPageView.getMocName()).thenReturn("EUtranRelationTDD");
        coperTypeFakeMoField = new FakeMoField(mockPageView, null);
        when(mockPageView.getMoField(LteCMConstants.FIELD_COPERTYPE)).thenReturn(coperTypeFakeMoField);
    }

    @Before
    public void setup() throws RelationException
    {
        PowerMockito.mockStatic(ClntBoardUtils.class);
        bplTypeByCellAction = new BplTypeByCellAction(mockPageView, null);
    }

    @Test
    public void 当为BPL0时EUtranRelationTDD_coperType_为不可见() throws RelationException
    {
        when(ClntBoardUtils.getBplProductNumberByCellMoi(mockPageView, "cellMoi"))
                .thenReturn(LteCMConstants.BPL0_CODE);
        bplTypeByCellAction.editValueChangeAction(mockPageView, "sourceName", "cellMoi");
        assertThat(coperTypeFakeMoField.isVisible(), is(false));
    }

    @Test
    public void 当不为BPL0时EUtranRelationTDD_coperType_为可见() throws RelationException
    {
        when(ClntBoardUtils.getBplProductNumberByCellMoi(mockPageView, "cellMoi"))
                .thenReturn(LteCMConstants.BPL1_CODE);
        bplTypeByCellAction.editValueChangeAction(mockPageView, "sourceName", "cellMoi");
        assertThat(coperTypeFakeMoField.isVisible(), is(true));
    }
```
**注：注解是必不可少的
@RunWith(PowerMockRunner.class)
@PrepareForTest(ClntBoardUtils.class)**

**接口提取**
coperTypeFakeMoField = new FakeMoField(mockPageView, null);
由于真实的MoField方法比较复杂，难使用。因此采用FakeMoField取代真实的MoField的行为。FakeMoField与MoField实现同一父类。
```java
public class FakeMoField extends MoField
{
    boolean enabled = true;
    boolean isVisible;

    public void setEnabled(boolean isEnabled)
    {
        this.enabled = isEnabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public FakeMoField(PageView view, FieldInfoObject field)
    {
        super(view, field);
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    @Override
    public Object getMoValue()
    {
        return null;
    }

    @Override
    public void setMoValue(Object o)
    {

    }

    @Override
    public String getDisplayValue(Object o)
    {
        return null;
    }

    @Override
    public Object getModelData()
    {
        return null;
    }

    @Override
    public void setModelData(Object o)
    {

    }
}
```

### 实践二 如何mock构造方法

```java
源代码：
public class BandWidthAction extends AbstractLteMoFieldAction {
    private static final DebugPrn debugPrn = new DebugPrn(BandWidthAction.class.getName());

    public BandWidthAction(PageView page, FieldInfoObject field) {
        super(page, field);
    }

    @Override
    public void createValueChangeAction(PageView page, String sourceName, String sourceValue) throws RelationException {
        EUtranCellRules rules = new EUtranCellTDDRules();
        LteCmClntUtil.setMoFieldValue(page, sourceName, LteCMConstants.FIELD_CFI, rules.getCFIValue(page));
        LteCmClntUtil.setMoFieldValue(page, sourceName, LteCMConstants.FIELD_MAXUERBNUMUL, rules.getMaxUeRbNumULValue(sourceValue));
        LteCmClntUtil.setMoFieldValue(page, sourceName, LteCMConstants.FIELD_MAXUERBNUMDL, rules.getMaxUeRbNumDlValue(sourceValue));
        LteCmClntUtil.setMoFieldValue(page, sourceName, LteCMConstants.FIELD_UPINTERFFREQEFFTHR, rules.getUpInterfFreqEffThrDefaultValue(sourceValue));
        LteCmClntUtil.setMoFieldValue(page, sourceName, LteCMConstants.FIELD_CCEADAPTMOD, rules.getCceAdaptModValue(page));
        if (rules.setCellCapaLeveIndEnable(page))
            LteCmClntUtil.setMoFieldValue(page, sourceName, LteCMConstants.FIELD_CELLCAPALEVEIND, 2);
        setMasterDefaultValue(page, sourceName, sourceValue);
    }

    @Override
    public void editValueChangeAction(PageView page, String sourceName, String sourceValue) throws RelationException {
        EUtranCellRules rules = new EUtranCellTDDRules();
        if (rules.setCellCapaLeveIndEnable(page))
            LteCmClntUtil.setMoFieldValue(page, sourceName, LteCMConstants.FIELD_CELLCAPALEVEIND, 2);
    }

    private void setMasterDefaultValue(PageView page, String bandWidthName, String bandWidthValue) {
        Map<String, Object> mocParaMap = page.getValues();
        MoField moField;
        FieldInfoObject fieldInfoObject;
        for (Map.Entry<String, Object> entry : mocParaMap.entrySet()) {
            if (entry.getKey().equals(bandWidthName)) {
                continue;
            }
            moField = page.getMoField(entry.getKey());
            fieldInfoObject = moField.getFieldInfoObject();
            if (fieldInfoObject.isExistMasterDefaultValue(mocParaMap)) {
                moField.setMoValue(fieldInfoObject.getWsfDefaultValue(mocParaMap));
                debugPrn.keyInfo("***************When Bandwidth=" + bandWidthValue + " Master define:Field name " + entry.getKey() + "::::value:" + fieldInfoObject.getWsfDefaultValue(mocParaMap));
            }
        }
    }
}
```
EUtranCellTDDRules如果构建真实的EUtranCellTDDRules对象比较困难，此处就可以通过whenNew(EUtranCellTDDRules.class).withNoArguments().thenReturn(mockEUtranCellTDDRules);的方式返回一个MOCK的EUtranCellTDDRules。UT测试代码如下：
```java
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BandWidthAction.class, LteCmClntUtil.class })
public class BandWidthActionTest
{
    PageView mockPageView = mock(PageView.class);
    EUtranCellTDDRules mockEUtranCellTDDRules = mock(EUtranCellTDDRules.class);
    FieldInfoObject mockFieldInfoObject = mock(FieldInfoObject.class);
    BandWidthAction bandWidthAction = new BandWidthAction(mockPageView, mockFieldInfoObject);
    FakeMoField mockMoField = mock(FakeMoField.class);
    HashMap<String, Object> paraMap = new HashMap<String, Object>();

    @Before
    public void setup() throws Exception
    {
        paraMap.clear();
        paraMap.put("sourceName", "sourceValue");
        mockStatic(LteCmClntUtil.class);
        doNothing().when(LteCmClntUtil.class, "setMoFieldValue", anyObject(), anyString(), anyString(), anyObject());
        whenNew(EUtranCellTDDRules.class).withNoArguments().thenReturn(mockEUtranCellTDDRules);

        when(mockEUtranCellTDDRules.getCFIValue(mockPageView)).thenReturn("");
        when(mockEUtranCellTDDRules.getMaxUeRbNumULValue(anyString())).thenReturn("");
        when(mockEUtranCellTDDRules.getUpInterfFreqEffThrDefaultValue(anyString())).thenReturn("");
        when(mockEUtranCellTDDRules.getCceAdaptModValue((PageView) anyObject())).thenReturn("");
        when(mockEUtranCellTDDRules.setCellCapaLeveIndEnable((PageView) anyObject())).thenReturn(false);

        when(mockPageView.getValues()).thenReturn(paraMap);
        when(mockMoField.getFieldInfoObject()).thenReturn(mockFieldInfoObject);
        when(mockFieldInfoObject.isExistMasterDefaultValue(paraMap)).thenReturn(true);
        when(mockFieldInfoObject.getWsfDefaultValue(paraMap)).thenReturn("bbb");
        doNothing().when(mockMoField).setMoValue("bbb");
    }

    @Test
    public void 测试创建场景_当存在与bandwidth不相等的值时setMoValue被调用() throws Exception
    {
        paraMap.put("not_equal_with_sourceName", "aaa");
        when(mockPageView.getMoField("not_equal_with_sourceName")).thenReturn(mockMoField);
        bandWidthAction.createValueChangeAction(mockPageView, "sourceName", "sourceValue");
        verify(mockMoField).setMoValue("bbb");
    }
}
```
**any参数**
doNothing().when(LteCmClntUtil.class, "setMoFieldValue", anyObject(), anyString(), anyString(), anyObject());
**多个需要准备的类，以数组的形式组织**
@PrepareForTest({ BandWidthAction.class, LteCmClntUtil.class })


### 实践三 如何测试私有方法

```java
源代码：
public class EUtranCellTDDSceneAction extends AbstractLteMoFieldAction {

    public EUtranCellTDDSceneAction(PageView page, FieldInfoObject field) {
        super(page, field);
    }

.........

    private void setRelatedCellLocalIdVisible(PageView page) throws RelationException {
        Object scene = LteCmClntUtil.getNeSceneByMoi(page);
        if (scene != null) {
            boolean visible = scene.toString().equals("101") || scene.toString().equals("105");
            page.getMoField(LteCMConstants.FIELD_RELATEDCELLLOCALID).setVisible(visible);
        }
    }
}
```
有时候，不得以必须测试私有方法，可以通过：Whitebox.invokeMethod(mockEUtranCellTDDSceneAction,"setRelatedCellLocalIdVisible",mockPageView);这种方式进行测试。
UT代码如下
```java
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LteCmClntUtil.class })
public class EUtranCellTDDSceneActionTest
{
    PageView mockPageView = mock(PageView.class);
    MoField mockMoField = mock(MoField.class);
    EUtranCellTDDSceneAction mockEUtranCellTDDSceneAction  = mock(EUtranCellTDDSceneAction.class);


    @Before
    public void setup() throws Exception
    {
        PowerMockito.mockStatic(LteCmClntUtil.class);
        when(mockPageView.getMoField(LteCMConstants.FIELD_RELATEDCELLLOCALID)).thenReturn(mockMoField);
        doNothing().when(mockMoField,"setVisible",anyBoolean());
    }

    @Test
    public void 当场景为101时MO字段设置为可见() throws Exception
    {
        when(LteCmClntUtil.getNeSceneByMoi(mockPageView)).thenReturn("101");

        Whitebox.invokeMethod(mockEUtranCellTDDSceneAction,"setRelatedCellLocalIdVisible",mockPageView);

        verify(mockMoField).setVisible(true);
    }
}
```
**注：不建议直接测试私有方法，一般私有方法的业务需要通过调用处进行调用测试业务**

### 实践四 如何向对象中赋值
```java
源代码：
public String getfreqBandInd(PageView page, String ecellEquipmentMoi) throws RelationException
    {
        .......

        MoField earfcndlField = page.getMoField(radioConstant.getEarfcn());
        .......
     }
```
方法getfreqBandInd中用到了radioConstant，此时可以通过下面这种形式给对象赋值
Whitebox.setInternalState(eUtranCellTDDRules, "radioConstant", mockMoName);UT代码如下：
```java
@RunWith(PowerMockRunner.class)
@PrepareForTest({LteCmClntUtil.class})
public class EUtranCellTDDRules_getfreqBandInd_Test {
    EUtranCellTDDRules eUtranCellTDDRules = new EUtranCellTDDRules();
    PageView mockPageView = mock(PageView.class);
    String ecellEquipmentMoi = "ecellEquipmentMoi";
    IMoName mockMoName = mock(IMoName.class);
    MoField mockMoField = mock(MoField.class);

    @Before
    public void setUp() throws Exception {
        Whitebox.setInternalState(eUtranCellTDDRules, "radioConstant", mockMoName);
        }
```

### 实践五 如何采用verify验证void方法

```java
源代码：
public class BplTypeAction extends AbstractLteMoFieldAction
{
    public BplTypeAction(PageView page, FieldInfoObject field)
    {
        super(page, field);
    }

    public void setCellModVisible(PageView page, String bplTypeValue)
    {
        MoField cellModMoField = page.getMoField(LteCMConstants.FIELD_CELLMOD);
        if (bplTypeValue.equals("1"))
        {
            cellModMoField.setVisible(true);
        }
        else if (bplTypeValue.equals("0"))
        {
            cellModMoField.setVisible(false);
        }
    }
}
```
由于setCellModVisible方法没有返回值，所以不能进行返回值的验证，此时可以通过使用verify进行方法调用的验证。
注：verify验证，传入参数在验证范围内。
```java
UT代码：
public class BplTypeActionTest
{
    PageView mockPageView = mock(PageView.class);
    FieldInfoObject mockFieldInfoObject = mock(FieldInfoObject.class);
    BplTypeAction bplTypeAction = new BplTypeAction(mockPageView, mockFieldInfoObject);
    MoField mockMoField = mock(MoField.class);

    @Before
    public void setup() throws Exception
    {
        when(mockPageView.getMoField(anyString())).thenReturn(mockMoField);
        doNothing().when(mockMoField, "setVisible", anyBoolean());
    }

    @Test
    public void 当bplType为1时设置为可见() throws Exception
    {
        bplTypeAction.setCellModVisible(mockPageView, "1");
        verify(mockMoField).setVisible(true);
    }

    @Test
    public void 当bplType为0时设置为不可见() throws Exception
    {
        bplTypeAction.setCellModVisible(mockPageView, "0");
        verify(mockMoField).setVisible(false);
    }
}
```

### 实践六 mock的对象如何进行真实方法的调用

```java
@Override
    public String getMatrixTypeValueByBPL(PageView page, String bplNum, String refEquip) throws RelationException
    {
        if (!LteCmClntUtil.isBPL0(bplNum))
            return "1";

        if (!LteCmClntUtil.isParaInvalid(page, LteCMConstants.FIELD_SFASSIGNMENT))
        {
            String sfAssignmentValue = page.getMoField(LteCMConstants.FIELD_SFASSIGNMENT).getMoValue().toString();
            if ("0".equals(sfAssignmentValue))
            {
                return "0";
            }
        }
        List<Map<String, Object>> lstcellEquipResult = LteCmClntUtil
                .select(page, refEquip, TDDMoName.getInstance().getECellEquipmentFunction(),
                        new String[] { "upActAntBitmap" }, null, null);
        if (lstcellEquipResult.size() > 0)
        {
            String upAntBitmap = lstcellEquipResult.get(0).get(LteCMConstants.FIELD_UPACTANTBITMAP).toString();
            int temp = LteCmClntUtil.getAntNum(Integer.parseInt(upAntBitmap));
            if (temp == 8)
            {
                return "0";
            }
        }

        return "1";
    }
```
方法中对于LteCmClntUtil.isBPL0与LteCmClntUtil.getAntNum调用真实的方法；
LteCmClntUtil.class.isParaInvalid采用MOCK对象的返回值；
when(LteCmClntUtil.isBPL0(anyString())).thenCallRealMethod();
        when(LteCmClntUtil.getAntNum(anyInt())).thenCallRealMethod();
        when(LteCmClntUtil.class, "isParaInvalid", mockPageView, LteCMConstants.FIELD_SFASSIGNMENT).thenReturn(false);

测试代码如下：
```java
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LteCmClntUtil.class })
public class EUtranCellTDDRules_getMatrixTypeValueByBPL_Test
{

    EUtranCellTDDRules eUtranCellTDDRules = new EUtranCellTDDRules();
    PageView mockPageView = mock(PageView.class);
    MoField mockMoField = mock(MoField.class);
    List<Map<String, Object>> upActAntBitList;

    @Before
    public void setUp() throws Exception
    {
        PowerMockito.mockStatic(LteCmClntUtil.class);
        when(LteCmClntUtil.isBPL0(anyString())).thenCallRealMethod();
        when(LteCmClntUtil.getAntNum(anyInt())).thenCallRealMethod();
        when(LteCmClntUtil.class, "isParaInvalid", mockPageView, LteCMConstants.FIELD_SFASSIGNMENT).thenReturn(false);
        when(mockPageView.getMoField(anyString())).thenReturn(mockMoField);
        upActAntBitList = new ArrayList<Map<String, Object>>();
    }
}
```

### 实践七 UT代码也是JAVA代码，抽象类继承等编程方式也是可以采用的

举例如下：
```java
父类：
public class EUtranCellRulesTest_getFlagSwiModeValue_BPL1_Setup
{
    public PageView mockPageView = mock(PageView.class);
    public MoField mockCellRSPortNumMoField = mock(MoField.class);
    public MoField mockCellCapaLeveIndMoField = mock(MoField.class);
    public MoField mockSceneCfgMoField = mock(MoField.class);

    public void setUp() throws Exception
    {
        PowerMockito.mockStatic(LteCmClntUtil.class);
        when(LteCmClntUtil.class, "isParaInvalid", mockPageView, LteCMConstants.FIELD_CELLRSPORTNUM).thenReturn(false);
        when(LteCmClntUtil.isParaInvalid(mockPageView, new String[] { LteCMConstants.FIELD_SCENECFG, LteCMConstants.FIELD_CELLCAPALEVEIND })).thenReturn(false);
        when(mockPageView.getMoField(LteCMConstants.FIELD_CELLRSPORTNUM)).thenReturn(mockCellRSPortNumMoField);
        when(mockPageView.getMoField(LteCMConstants.FIELD_CELLCAPALEVEIND)).thenReturn(mockCellCapaLeveIndMoField);
        when(mockPageView.getMoField(LteCMConstants.FIELD_SCENECFG)).thenReturn(mockSceneCfgMoField);
        when(LteCmClntUtil.getNeSceneByMoi(mockPageView)).thenReturn("102");
        when(mockCellRSPortNumMoField.getMoValue()).thenReturn("2");
    }

}
```
EUtranCellTDDRulesTest_getFlagSwiModeValue_BPL1_Test 继承了父类：EUtranCellRulesTest_getFlagSwiModeValue_BPL1_Setup
```java
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LteCmClntUtil.class })
public class EUtranCellTDDRulesTest_getFlagSwiModeValue_BPL1_Test extends
        EUtranCellRulesTest_getFlagSwiModeValue_BPL1_Setup
{
    EUtranCellRules eUtranCellRules = new EUtranCellTDDRules();

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }
}
```
EUtranCellFDDRulesTest_getFlagSwiModeValue_BPL1_Test 继承了父类：EUtranCellRulesTest_getFlagSwiModeValue_BPL1_Setup
```java
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LteCmClntUtil.class })
public class EUtranCellFDDRulesTest_getFlagSwiModeValue_BPL1_Test
        extends EUtranCellRulesTest_getFlagSwiModeValue_BPL1_Setup
{
    EUtranCellRules eUtranCellRules = new EUtranCellFDDRules();

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }
```
### 实践八 如何mock多态方法

```java
静态类中的多态方法
public class LteCmClntUtil {
public static boolean isParaInvalid(PageView page, String[] paraNames) {
        MoField field;
        for (String name : paraNames) {
            field = page.getMoField(name);
            if (field == null || field.getMoValue() == null || "".equals(field.getMoValue())) {
                return true;
            }
        }
        return false;
    }

    public static Object getNeSceneByMoi(PageView page) throws RelationException {
        List<Map<String, Object>> data = select(page, LteCmClntUtil.getNeMoiByPage(page), LteCMConstants.MOC_NEMANAGEDELEMENT, new String[]{LteCMConstants.FIELD_SCENE}, null, null);
        if (data.size() > 0) {
            return data.get(0).get(LteCMConstants.FIELD_SCENE);
        }
        debugPrn.error("query scene from management is null, please check db.");
        return "";
    }
}
```
**
Method method(Class<?> declaringClass, String methodName, Class<?>... parameterTypes)
PowerMockito提供的静态方法method可以用来识别多态，UT代码如下
**
```java
@RunWith(PowerMockRunner.class)
@PrepareForTest(LteCmClntUtil.class)
public class ECellEquipmentFunctionFDDRules_getBplPortValueByrRefBpDevice_Test
{
    @Before
    public void setUp() throws Exception
    {
when(LteCmClntUtil.class,method(LteCmClntUtil.class,"isParaInvalid",PageView.class,String.class)).withArguments(anyObject(),anyString()).thenReturn(true);
        when(LteCmClntUtil.class,method(LteCmClntUtil.class,"isParaInvalid",PageView.class,String[].class)).withArguments(anyObject(),anyObject()).thenReturn(false);
    }

    @Test
    public void 用来测试多态() throws Exception
    {
        assertTrue(LteCmClntUtil.isParaInvalid(mockPageView,"s"));
        assertFalse(LteCmClntUtil.isParaInvalid(mockPageView,new String[]{"s","s"}));
    }
}
```
### 实践九 如何利用spy进行测试
```java
原代码：
    public String getBplPortValueByrRefBpDevice(PageView page, String bplMoi, String bplNum) throws RelationException {
        //待确认bplport的适用基带板之后，更新此逻辑
        if (!LteCMConstants.BPL0_CODE.equals(bplNum) && !LteCMConstants.BPL1_CODE.equals(bplNum)) {
            return "";
        }
        MoField portField = page.getMoField(LteCMConstants.FIELD_BPLPORT);
        if (LteCmClntUtil.isParaInvalid(page,LteCMConstants.FIELD_REFRFDEVICE)  || portField == null) {
            return "";
        }
        MoField refRfDeviceField = page.getMoField(LteCMConstants.FIELD_REFRFDEVICE);
        String refRfDeviceMoi = refRfDeviceField.getMoValue().toString().split(";")[0];
        String bplSmoi = SMoiOperator.getSMOIByMoi(bplMoi);
        String rruMoi = CmUtil.getParentMoi(refRfDeviceMoi, CmUtil.getMoLevel(refRfDeviceMoi) - 2);
        String rruSmoi = SMoiOperator.getSMOIByMoi(rruMoi);
        String where = "ref2FiberDevice like '" + rruSmoi + ",%' AND ref1FiberDevice like '" + bplSmoi + ",%'";
        String[] fiberCablePara = new String[]{LteCMConstants.FIELD_MOI, LteCMConstants.FIELD_REF1FIBERDEVICE};
        List<Map<String, Object>> lstFiberCablelResult = LteCmClntUtil.select(page, null, LteCMConstants.MOC_FIBERCABLE, fiberCablePara, where, null);
        if (lstFiberCablelResult != null && lstFiberCablelResult.size() == 1) {
            String bplFiberDeviceMoi = lstFiberCablelResult.get(0).get(LteCMConstants.FIELD_REF1FIBERDEVICE).toString();
            String[] fibelDevicepara = new String[]{LteCMConstants.FIELD_PORT};
            List<Map<String, Object>> lstFiberDeviceResult = LteCmClntUtil.select(page, bplFiberDeviceMoi, LteCMConstants.MOC_FIBERDEVICE, fibelDevicepara, null, null);
            if (lstFiberDeviceResult != null && lstFiberDeviceResult.size() > 0) {
                return lstFiberDeviceResult.get(0).get(LteCMConstants.FIELD_PORT).toString();
            }
        }
        return "";
}

安全重构后的代码：
    public String getBplPortValueByrRefBpDevice(PageView page, String bplMoi, String bplNum) throws RelationException {
        if (isSatisfy(page, bplNum))
            return "";
        String refRfDeviceMoi = getRefRfDeviceMoi(page);
        String bplSmoi = SMoiOperator.getSMOIByMoi(bplMoi);
        String rruMoi = CmUtil.getParentMoi(refRfDeviceMoi, CmUtil.getMoLevel(refRfDeviceMoi) - 2);
        String rruSmoi = SMoiOperator.getSMOIByMoi(rruMoi);
        String where = "ref2FiberDevice like '" + rruSmoi + ",%' AND ref1FiberDevice like '" + bplSmoi + ",%'";
        String[] fiberCablePara = new String[]{LteCMConstants.FIELD_MOI, LteCMConstants.FIELD_REF1FIBERDEVICE};
        List<Map<String, Object>> lstFiberCablelResult = LteCmClntUtil.select(page, null, LteCMConstants.MOC_FIBERCABLE, fiberCablePara, where, null);
        if (lstFiberCablelResult != null && lstFiberCablelResult.size() == 1) {
            String bplFiberDeviceMoi = lstFiberCablelResult.get(0).get(LteCMConstants.FIELD_REF1FIBERDEVICE).toString();
            String[] fibelDevicepara = new String[]{LteCMConstants.FIELD_PORT};
            List<Map<String, Object>> lstFiberDeviceResult = LteCmClntUtil.select(page, bplFiberDeviceMoi, LteCMConstants.MOC_FIBERDEVICE, fibelDevicepara, null, null);
            if (lstFiberDeviceResult != null && lstFiberDeviceResult.size() > 0) {
                return lstFiberDeviceResult.get(0).get(LteCMConstants.FIELD_PORT).toString();
            }
        }
        return "";
    }

    protected String getRefRfDeviceMoi(PageView page)
    {
        MoField refRfDeviceField = page.getMoField(LteCMConstants.FIELD_REFRFDEVICE);
        return refRfDeviceField.getMoValue().toString().split(";")[0];
    }

    protected boolean isSatisfy(PageView page, String bplNum)
    {
        return !isBpl0orBpl1(bplNum) || portIsNull(page) || isRefRfDeviceParaInvalid(page);
    }

    private boolean isRefRfDeviceParaInvalid(PageView page)
    {
        return LteCmClntUtil.isParaInvalid(page, LteCMConstants.FIELD_REFRFDEVICE);
    }

    private boolean portIsNull(PageView page)
    {
        MoField portField = getBplPortField(page);
        return portField == null;
    }

    private MoField getBplPortField(PageView page)
    {
        return page.getMoField(LteCMConstants.FIELD_BPLPORT);
    }

    private boolean isBpl0orBpl1(String bplNum)
    {
        return LteCMConstants.BPL0_CODE.equals(bplNum) || LteCMConstants.BPL1_CODE.equals(bplNum);
    }
```

```java
UT代码：
@RunWith(PowerMockRunner.class)
@PrepareForTest(LteCmClntUtil.class)
public class ECellEquipmentFunctionFDDRules_getBplPortValueByrRefBpDevice_Test
{
    ECellEquipmentFunctionFDDRules eCellEquipmentFunctionFDDRules;
    PageView mockPageView = mock(PageView.class);
    @Before
    public void setUp() throws Exception
    {
        HashMap<String, Object> lstFiberCablelResultMap = new HashMap<String, Object>();
        ArrayList<Map<String, Object>> lstFiberCablelResultList = new ArrayList<Map<String, Object>>();
        lstFiberCablelResultList.add(lstFiberCablelResultMap);
        lstFiberCablelResultMap.put("ref1FiberDevice","ManagedElement=220,Equipment=1,Rack=1,SubRack=1,Slot=4,PlugInUnit=1,SdrDeviceGroup=1,FiberDeviceSet=1,FiberDevice=1");

        HashMap<String, Object> lstFiberDeviceResultMap = new HashMap<String, Object>();
        ArrayList<Map<String, Object>> lstFiberDeviceResultList = new ArrayList<Map<String, Object>>();
        lstFiberDeviceResultList.add(lstFiberDeviceResultMap);
        lstFiberDeviceResultMap.put("port","0");

        eCellEquipmentFunctionFDDRules = spy(new ECellEquipmentFunctionFDDRules());
        doReturn("ManagedElement=220,Equipment=1,Rack=51,SubRack=1,Slot=1,PlugInUnit=1,SdrDeviceGroup=1,RfDeviceSet=1,RfDevice=1").when(eCellEquipmentFunctionFDDRules).getRefRfDeviceMoi(mockPageView);
        doReturn(false).when(eCellEquipmentFunctionFDDRules).isSatisfy(mockPageView,"bplNum");
        mockStatic(LteCmClntUtil.class);
        when(LteCmClntUtil.class,"select",anyObject(),anyString(),anyString(),anyObject(),anyString(),anyMap()).thenReturn(lstFiberCablelResultList).thenReturn(lstFiberDeviceResultList);
        when(LteCmClntUtil.class,method(LteCmClntUtil.class,"isParaInvalid",PageView.class,String.class)).withArguments(anyObject(),anyString()).thenReturn(true);
        when(LteCmClntUtil.class,method(LteCmClntUtil.class,"isParaInvalid",PageView.class,String[].class)).withArguments(anyObject(),anyObject()).thenReturn(false);
    }

    @Test
    public void 当参数合法时返回端口号0() throws Exception
    {
        assertThat(eCellEquipmentFunctionFDDRules.getBplPortValueByrRefBpDevice(mockPageView,"ManagedElement=220,Equipment=1,Rack=1,SubRack=1,Slot=4,PlugInUnit=1,SdrDeviceGroup=1","bplNum"),is("0"));
    }
}
```
### 实践十 mock与spy的区别
mock一个类后，mock实例的所有方法均为假方法，想要调用真实方法需要使用thenCallRealMethod()；相反，spy一个类后，spy实例中所有方法均为真实方法，类似于直接new一个实例，对想要模拟的方法单独进行设置；

如上实践九安全重构后的代码，需要对protected String getRefRfDeviceMoi(PageView page)，protected boolean isSatisfy(PageView page, String bplNum)两个方法进行模拟操作

写UT之前进行一定的安全重构操作是必须的，会使得测试相当简单，而不用像重构前进行一大堆的mock，费了力成效也不大;

### 实践十一 doReturn与thenReturn的区别
thenReturn是会进入到方法体真正执行一遍，然后再返回所设置的返回值；
doReturn则不会进入方法体，而是直接返回所设置的返回值；

如实践九：
如果将doReturn改为thenReturn调用则会报错
protected String getRefRfDeviceMoi(PageView page)
protected boolean isSatisfy(PageView page, String bplNum)
目前发现doReturn不能调用private方法，此处修饰符修改为protected

### 实践十二 thenAnswer用法

适用于mock同一函数多次调用场景，多次调用mock结果不同。

用法示例：
```java
when(LteWsfTool.class, "query", anyObject()).thenAnswer(Answer接口实例)
```

Answer接口实现示例:
```java
@Override
public Object answer(InvocationOnMock invocation) throws Throwable {
        Object[] arguments = invocation.getArguments(); //获取入参列表
        QueryCondition condition = (QueryCondition) arguments[0];
        if (compareCondition(condition, expectedQueryCellCondition()))
            return cellResult(); //根据不同入参构造返回结果
        if (compareCondition(condition, expectedQueryEquipCondition()))
            return equipResult();
        if (compareCondition(condition, expectedQueryBoardCondition()))
            return boardResult();
		...
    }
```

### 实践十三 assertThat匹配Matcher用法

**一般匹配**

| 用法 | 说明|
|--------|--------|
|allOf(greaterThan(8),lessThan(16))|allOf表明所有条件必须都成立才通过，相当于&&|
|anyOf(greaterThan(16),lessThan(8))|anyOf表明只要有一个成立则通过，相当于&brvbar;&brvbar;|
|anything()|anything表明无论什么条件，永远为true|
|is(T t)|前面待测的object等于后面给出的object，则通过|
|not(T t)|和is匹配符正好相反|


**字符串相关匹配**

| 用法 | 说明|
|--------|--------|
|containsString("SubString")|包含子字符串则通过|
|endsWith("SubString")|以子字符串结尾则通过|
|startsWith("SubString")|以子字符串开始则通过|
|equalTo(expectedValue)|是否相等|
|equalToIgnoringCase("SubString")|忽略大小写的情况下是否等于|
|equalToIgnoringWhiteSpace("SubString")|忽略头尾的任意个空格的情况下是否等于|

**数值相关匹配**

| 用法 | 说明|
|--------|--------|
|closeTo(20.0,0.5)|预期的浮点型数在20.0±0.5范围之内则通过|
|greaterThan(16.0)|大于16.0则通过|
|lessThan(16.0)|小于16.0则通过|
|greaterThanOrEqualTo(16.0)|大于等于16.0则通过|
|lessThanOrEqualTo(16.0)|小于等于16.0则通过|

**collection相关匹配符**

| 用法 | 说明|
|--------|--------|
|hasEntry("key","value")|Map对象含有一个键值为"key"对应元素值为"value"的Entry项则通过|
|hasItem("element")|迭代对象iterableObject含有元素“element”项则通过|
|hasKey("key")|Map对象含有键值“key”则通过|
|hasValue("value")|Map对象含有元素值“value”通过|

### 实践十四 SuppressStaticInitialization的用法
**问题**
我们希望Mock的一个类，定义了一个static块，其中又调用了私有的静态方法。在这 个私有静态方法中，依赖了其他的一些对象，这些对象还牵扯到服务容器的问题。即使以静态的方式Mock了该类，仍然逃不过运行static块的命运，换言 之，仍然需要依赖服务容器。

**解决方法**
使用@SuppressStaticInitializationFor标注压制静态初始化。
该标注中需要传入字符串类型的目标类型的全名。假设EmployeeTableUtil有一个static块是我们需要绕过的，它的类全名为 com.agiledon.powermock.EmployeeTableUtil：

```java
@RunWith(PowerMockRunner.class)
@PrepareForTest(EmployeeTableUtil.class)
@SuppressStaticInitializationFor({"com.agiledon.powermock.EmployeeTableUtil", "com.agiledon.powermock.MockedObjectA"})
public class EmployeeRepositoryTest {}
```
### 实践十五 mock枚举实现的单例
一种优雅实现单例的方法是利用enum特性，但这种方式创建的单例一个问题就是可测试性不强，即实例很难通过依赖注入的方式替换为Fake对象。
但可以利用强大的PowerMock，Whitebox.setInternalState()方法设置mock对象。
下面举例说明：
```java
// 被测代码
public enum SingletonObject {
    INSTANCE;
    private int num;
    protected void setNum(int num) {
        this.num = num;
    }
    public int getNum() {
        return num;
    }
}

public class SingletonConsumer {
    public String consumeSingletonObject() {
        return String.valueOf(SingletonObject.INSTANCE.getNum());
    }
}

// 测试代码
@RunWith(PowerMockRunner.class)
@PrepareForTest({SingletonObject.class})
public class SingletonConsumerTest {
    @Test
    public void testConsumeSingletonObject() throws Exception {
        SingletonObject mockInstance = mock(SingletonObject.class);
        Whitebox.setInternalState(SingletonObject.class, "INSTANCE", mockInstance);
        when(mockInstance.getNum()).thenReturn(42);
        assertEquals("42", new SingletonConsumer().consumeSingletonObject());
    }
}
```
