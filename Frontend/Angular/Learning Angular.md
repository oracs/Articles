

## 环境搭建
https://wiki.zte.com.cn/pages/viewpage.action?pageId=33832016

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
说明：

- input text的ng-model定义为personName。
- button的ng-click事件调用search()方法。
- {{person.xxx}}是调用数据模型后返回的结果。

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
说明：

- $scope.search定义了search()方法，$scope.personName是view上的input text的数据模型名称。
- $http.get()是调用后端search请求，带了一个参数personName。返回结果是person对象。


## Spring Boot的数据访问
Spring Data是Spring数据访问的系列工程，它支持对数据的统一访问标准。
主要支持的数据源包括：
**Spring Data JPA，**
```java
Maven坐标：
groupId: org.springframework.data
artifactId: spring-data-jpa
```

其余常见的数据源包括：MongoDB, Neo4j, Redis, ElasticSearch, Cassandra等。

CrudRepository接口定义了常用的操作：save(),find(),delete()等。





