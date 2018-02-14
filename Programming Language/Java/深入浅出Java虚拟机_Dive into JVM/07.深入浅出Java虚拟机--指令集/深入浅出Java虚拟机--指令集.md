**修订记录**


| 时间 | 内容 |
|--------|--------|
| 2017.06.20 | 初稿 |



## 加载和存储指令

这类指令主要用于虚拟机栈 栈桢中的局部变量表和操作数栈之间进行的操作。



| 用途 | 指令 |
|--|--|
| 将一个局部变量加载到操作数栈 | `iload`, `iload_<n>`, `lload`, `lload_<n>`, `fload`, `fload_<n>`, `dload`, `dload_<n>`, `aload`, `aload_<n>` |
| 将值从操作数栈取出存入到局部变量表 | `istore`, `istore_<n>`, `lstore,` `lstore_<n>`, `fstore`, `fstore_<n>`, `dstore`, `dstore_<n>`, `astore`, `astore_<n>` |
| 将一个常量加载到操作数栈 | `bipush`, `sipush`, `ldc`, `ldc_w`, `ldc2_w`, `aconst_null`, `iconst_ml`, `iconst_<i>`, `iconst_<l>`, `iconst_<f>`, `iconst_<d>` |
| 扩充局部变量表的访问索引的指令  | wide |


iload_<n>表示load本地变量表的第n个数据


## 运算指令

这类指令主要是对两个操作数栈上的值进行计算，并将结果存入到操作栈顶。



| 用途 | 指令 |
|--|--|
| 加法指令  | iadd, ladd, fadd, dadd  |
| 减法指令 | isub, lsub, fsub, dsub   |
| 乘法指令 | imul, lmul, fmul, dmul  |
| 除法指令 | idiv, ldiv, fdiv, ddiv  |
| 求余指令 | irem, lrem, frem, drem  |
| 取反指令 | ineg, lneg, fneg, dneg  |
| 位移指令 | ishl, ishr, iushr, lshl, lushr |
| 按位或指令 | ior, lor |
| 按位与指令 | iand, land |
| 按位异或指令 | ixor, lxor |
| 局部变量自增指令 | iinc  |
| 比较指令 | dcmpg, dcmpl, fcmpg, fcmpl, lcmp |


## 类型转换指令

这类指令主要用于两个不用数值类型间的相互转换。JVM支持从小范围类型向大范围类型的直接转型。但如果是窄化类型转换（从大范围类型向小范围类型转换），必须显示的使用指令进行转换。

| 用途 | 指令 |
|--|--|
| 窄化转换指令 | i2b, i2c, i2s, l2i, l2i, f2i, f2l, d2i, d2l, d2f  |

## 对象创建与访问指令

| 用途 | 指令 |
|--|--|
| 创建类实例指令 | new |
| 创建数组的指令 | newarray, anewarray, multianewarray |
| 访问类字段和实例字段指令 | getfield, putfield, getstatic, putstatic |
| 把一个数组元素加载到操作数栈的指令 | baload, caload, saload, iaload, laload, faload, daload, aaload |
| 将操作数栈的值存储到数组元素中的指令 | bastore, castore, sastore, iastore, fastore, dastore, aastore  |
| 取数组长度的指令 | arraylength |
| 检查类实例类型的指令 | instanceof, checkcast |

## 操作数栈管理指令

| 用途 | 指令 |
|--|--|
| 将操作数栈栈顶一个或两个元素出栈 | pop, pop2 |
| 复制栈顶一个或两个元素并重新压入栈 | dup, dup2, dup_x1, dup2_x1, dup_x2, dup2_x2 |
| 将栈顶前2个元素互换 | swap |

## 控制转移指令

| 用途 | 指令 |
|--|--|
| 条件分支 | ifreq, iflt, ifle, ifne, ifgt, ifge, ifnull, ifnonnull, if_icmpeq, if_icmpne,<br> if_icmplt, if_icmpgt, if_icmple, if_icmpge, if_acmpeq, if_acmpne|
| 复合条件分支 | tableswitch, lookupswitch  |
| 无条件分支 | goto, goto_w, jsr, jsr_w, ret |

## 方法调用和返回指令

| 用途 | 指令 | |
|--|--| -- |
| 调用实例方法 | invokevirtual |  |
| 调用接口方法，会在运行时再确定一个实现此接口的对象 | invokeinterface | |
| 调用实例构造器方法，私有方法和父类方法 | invokespecial | |
| 调用静态方法 | invokestatic | |
| 运行时动态解析出调用点限定符所引用的方法，并调用 | invokedynamic | |

## 异常处理指令

| 用途 | 指令 |
|--|--|
| 异常处理指令 | athrow |

## 同步指令

| 用途 | 指令 |
|--|--|
| 开始同步 | monitorenter |
| 结束同步 | monitorexit |






