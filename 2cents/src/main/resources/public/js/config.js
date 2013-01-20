twocents.config(["$routeProvider", function($routeProvider) {
    $routeProvider.when("/", {
        templateUrl: "partials/login.html",
        controller: LoginCtrl
    }).when("/documents", {
        templateUrl: "partials/documents.html",
        controller: AssetListCtrl
    }).otherwise({
        redirectTo: "/"
    });
}]);

twocents.config(["$locationProvider", "$httpProvider", function($locationProvider, $httpProvider) {
    $locationProvider.html5Mode(true);
    $httpProvider.responseInterceptors.push('AuthResponseInterceptor');
}]);

twocents.run(["$rootScope", "$location", "$http", "Cookies", function($rootScope, $location, $http, Cookies) {

    var URL_PREFIX = "/v1";

    $rootScope.user = {};

    $rootScope.assetDuplicate = {};

    $rootScope.headerTemplate = "partials/header_app.html";

    $rootScope.header = {};

    $rootScope.serviceUrls = {
        getAuthenticationUrl: function(user) {
            return URL_PREFIX + "/users/" + user + "/authentication";
        },
        getAssetsUrl: function() {
            return URL_PREFIX + "/assets";
        },
        getAssetModelsUrl: function() {
            return URL_PREFIX + "/assetmodels";
        },
        getLocationsUrl: function() {
            return URL_PREFIX + "/locations";
        }
    }

    if (Cookies.hasItem('token')) {
        $http.defaults.headers.common['Auth-Token'] = Cookies.getItem('token');
        if ($location.path() === "/") {
            $location.path('/asset-list');
        }
    }

    if (Cookies.hasItem('user')) {
        $rootScope.user = JSON.parse(Cookies.getItem('user'));
    }

}]);