var module = angular.module("routingCoupons", ['ngRoute']);
var myClient = {}	;
var clientFacade;

module.config(['$routeProvider', function ($routeProvider) {

	$routeProvider
	.when('/customer/:sendType', {
		templateUrl: 'customer.html',
		controller: 'sendNewObject'
	})
	.when('/customer/:sendType/:object', {
		templateUrl: 'customer.html',
		controller: 'sendNewObject'
	})
	.when('/coupon/:sendType/:object', {
		templateUrl: 'coupon.html',
		controller: 'sendNewObject'
	})
	.when('/coupon/:sendType/', {
		templateUrl: 'coupon.html',
		controller: 'sendNewObject'
	})
	.when('/company/:sendType/:object', {
		templateUrl: 'company.html',
		controller: 'sendNewObject'
	})
	.when('/viewCustomer/:typeViewObject', {
		templateUrl: 'viewCustomer.html',
		controller: 'viewObject'
	})
	.when('/viewCompany/:typeViewObject', {
		templateUrl: 'viewCompany.html',
		controller: 'viewObject'
	})
	.when('/viewCoupons/:typeViewObject', {
		templateUrl: 'viewCoupons.html',
		controller: 'viewObject'
	})
	.when('/company/:sendType', {
		templateUrl: 'company.html',
		controller: 'sendNewObject'
	})
	.when('/client/:typeViewObject', {
		templateUrl: 'viewCoupons.html',
		controller: 'viewObject'
	})
	.when('/viewIncome/:typeViewObject', {
		templateUrl: 'viewIncome.html',
		controller: 'viewObject'
	})
	.when('/viewIncome/:typeViewObject/:name', {
		templateUrl: 'viewIncome.html',
		controller: 'viewObject'
	})
	.when('/viewErrors/:typeViewObject', {
		templateUrl: 'viewErrors.html',
		controller: 'viewObject'
	})


}]);

module.controller("sendNewObject", function ($scope, $http, $routeParams) {
	$scope.myClient = myClient;
	$scope.object ={};
	$scope.change = false;
	if($routeParams.object!=null){
		$scope.object = JSON.parse($routeParams.object);
		$scope.change = true;
	}
	$scope.response="";
	$scope.sendType = $routeParams.sendType;
	
	
	$scope.sendObject = function() {		
		var objectString = JSON.stringify($scope.object)
		$http.post("rest/"+clientFacade+"/" +$scope.sendType, objectString)
		.success(function (data, status, headers, config) {
			$scope.response+="send object to DB:"+headers()['status']+".";
			$scope.messageToClient();
		}).
		error(function (data, status) {
			console.log(JSON.stringify(data));
			console.log(JSON.stringify(status));
		});
	
		};
		
		
		$(document).ready(function () {
		    $("#btnSubmit").click(function (event) {
		    	event.preventDefault();
		        fire_ajax_submit();
		    });
		});
			
		function fire_ajax_submit() {
			var fullName = document.getElementById('fileInput').value;
			var name = $scope.object.title +"_"+ fullName.slice(fullName.lastIndexOf("\\")+1);
			$scope.object.image = name;
			var form = $('#fileUploadForm')[0];
		    var data = new FormData(form);
		    data.append("name", name);

		    $("#btnSubmit").prop("disabled", true);
		    $.ajax({
		        type: "POST",
		        enctype: 'multipart/form-data',
		        url: "rest/companyService/sendPicture",
		        data: data,
		        processData: false,
		        contentType: false,
		        cache: false,
		        timeout: 600000,
		        success: function (data, textStatus, request) {
		            $("#btnSubmit").prop("disabled", false);
		            $scope.response = "upload file: " + request.getResponseHeader('status')+".";
		            $scope.sendObject();
		        },  error: function (e) {
		            console.log("ERROR : ", e);
		            $("#btnSubmit").prop("disabled", false);
		            alert("Error: " + data);
		        }
		    });

		}
		
		$scope.messageToClient = function()  {
			document.getElementById('messageToClient').hidden=false;
			var timeOut = setTimeout(function() {
				document.getElementById('messageToClient').hidden=true;
				$scope.response="";
			clearTimeout(timeOut)
			}, 7000)			
		}
		
})



module.controller("viewObject", function ($scope, $routeParams, $http, $location, $window) {
	$scope.myClient = myClient;
	$scope.clientFacade = clientFacade;
	$scope.type;
	$scope.response;
	$scope.allResponse=[];
	$scope.typeViewObject = $routeParams.typeViewObject
	$scope.validAfterDate = new Date();
	if ($routeParams.name!=null){
		$scope.typeViewObject = $routeParams.typeViewObject + "/"+ $routeParams.name
	}
	$http({
		method: "POST",
		url: "rest/"+clientFacade+"/" + $scope.typeViewObject,
	})
	.success((res) => {
		$scope.allResponse = res;
	}, function myError(res) {
		$scope.qaury = "bad";
	});
	
	
	$scope.update = function(object) {
		var objectString = JSON.stringify(object) 
		var url = 'index.html#/'+$scope.type.toLowerCase()+'/update'+$scope.type+'/'+ objectString ;
		$window.location.href = url;
	}
	
	$scope.viewIncome = function(id, typeQuestion) {
		var url = 'index.html#/viewIncome/'+ typeQuestion + '/'+ id ;
		$window.location.href = url;
	}
	$scope.remove = function(object) {
		var objectString = JSON.stringify(object)
		$http.post("rest/"+clientFacade+"/remove"+$scope.type, objectString)
		.success(function (data, status, headers, config) {
			$scope.response=headers()['status'];
			$scope.messageToClient();

		}).
		error(function (data, status) {
			console.log(JSON.stringify(data));
			console.log(JSON.stringify(status));
		});
	}
		
	$scope.purchase = function(object) {
		var objectString = JSON.stringify(object)
		$http.post("rest/"+clientFacade+"/purchaseCoupon", objectString)
		.success(function (data, status, headers, config) {
			$scope.response=headers()['status'];
			$scope.messageToClient();

		}).
		error(function (data, status) {
			console.log(JSON.stringify(data));
			console.log(JSON.stringify(status));
			});
	}
	
	$scope.priceFilter = function(coupon) {
		if ($scope.maxPrice==null){
			maxPrice = Number.MAX_VALUE;
		}else {maxPrice = $scope.maxPrice;}
		if($scope.minPrice==null){
			minPrice=0;
		}else {minPrice = $scope.minPrice;}
		return (minPrice<coupon.price && maxPrice>coupon.price)
	}
	
	$scope.dateFilter = function(coupon) {
		if (!(coupon.endDate instanceof Date)){
			coupon.endDate = new Date(Date.parse(coupon.endDate.slice(0,coupon.endDate.indexOf("T"))));	
		}
		if (!(coupon.startDate instanceof Date)){
			coupon.startDate = new Date(Date.parse(coupon.startDate.slice(0,coupon.startDate.indexOf("T"))));	
		}
		return ($scope.validAfterDate.getTime()<coupon.endDate.getTime());
	}
	
	$scope.messageToClient = function()  {
		document.getElementById('messageToClient').hidden=false;
		var timeOut = setTimeout(function() {
			document.getElementById('messageToClient').hidden=true;
			$scope.response="";
		clearTimeout(timeOut)
		}, 7000)			
	}
	

})

module.controller("sessionController", function ($scope, $http, $window) {
	$scope.object;
	$scope.status;
	$scope.clientFacade; 
	$http.post("rest/getSession/getSession")
	.success(function (data, status, headers, config) {
		clientFacade = headers('clientFacade');
		$scope.clientFacade = headers('clientFacade');
		$scope.object=data;
		myClient = data;
		if(clientFacade==null){
			$window.location.href = "Login.html" 
		}
	}).
	error(function (data, status) {
		console.log(JSON.stringify(data));
		console.log(JSON.stringify(status));
	});
	$scope.invalidate = function() {
		$http.post("rest/getSession/invalidate")
		.success(function (data, status, headers, config) {
			$window.location.href = "Login.html" 
		}).
		error(function (data, status) {
			console.log(JSON.stringify(data));
		});

	}
})


