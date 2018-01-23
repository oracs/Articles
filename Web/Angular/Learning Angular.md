## 入门
### 1.主界面：action.html
```html
<html lang="zh-cn" ng-app="actionApp">
<body>
...
          <ul class="nav navbar-nav">
            <li><a href="#/oper">后台交互</a></li>
            <li><a href="#/directive">自定义指令</a></li>
          </ul>
...		  
    <div class="content">
        <ng-view></ng-view> 
    </div>
</body>

```
说明：

- ng-app是设置名为actionApp的module。
- ng-view是动态加载actionApp中设置的views。
- "#/oper"和"#/directive"是路由地址。

### 2.app.js
```javascript
var actionApp = angular.module('actionApp',['ngRoute']);

actionApp.config(['$routeProvider' , function($routeProvider) {
	$routeProvider.when('/oper', { 
		controller: 'View1Controller', 
		templateUrl: 'views/view1.html', 
	}).when('/directive', {
		controller: 'View2Controller',
		templateUrl: 'views/view2.html',
	});
}]);
```

说明：

- 定义了module: actionApp。
- 路由：根据不同的地址，使用不同的控制器，并导航到对应页面。

### 3.view1.html
```html
<div class="row">
    <label for="attr" class="col-md-2 control-label">名称</label>
    <div class="col-md-2">
        <input type="text" class="form-control" ng-model="personName">
    </div>
    <div class="col-md-1">
        <button class="btn btn-primary" ng-click="search()">查询</button>
    </div>
</div>

<div class="row">
    <div class="col-md-4">
        <ul class="list-group">
            <li class="list-group-item">名字： {{person.name}}</li>
            <li class="list-group-item">年龄： {{person.age}}</li>
            <li class="list-group-item">地址： {{person.address}}</li>
        </ul>
    </div>
</div>
```

### 4.controller.js
```javascript
actionApp.controller('View1Controller', ['$rootScope', '$scope', '$http', function($rootScope, $scope,$http) {
    $scope.$on('$viewContentLoaded', function() {
    	console.log('页面加载完成');
    });

    $scope.search = function(){
      personName = $scope.personName;
      $http.get('search',{
    	  params:{personName:personName}
      }).success(function(data){
    	 $scope.person=data;
      });
     
    };
}]);

actionApp.controller('View2Controller', ['$rootScope', '$scope',  function($rootScope, $scope) {
    $scope.$on('$viewContentLoaded', function() {
    	console.log('页面加载完成');
    });
}]);
```