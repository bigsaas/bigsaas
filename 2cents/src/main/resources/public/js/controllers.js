/**
 * Created with JetBrains PhpStorm.
 * User: lucian
 * Date: 20/12/2012
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */

function LoginCtrl($scope, $location, $http, Cookies) {

    $scope.headerTemplate = 'partials/header_login.html';

    $scope.recoverPassword = function() {
        $('#forgotPassword').modal('hide');
        return false;
    }

    $scope.login = function() {
        $http.post($scope.serviceUrls.getAuthenticationUrl($scope.username), {
            "password": $scope.password
        }).success(function(data, status, headers, config) {
            if (200 === status) {
                var expirationDate = new Date();
                expirationDate.setDate(expirationDate.getDate() + 1);
                Cookies.setItem('token', headers()['auth-token'], expirationDate, '/', $location.host());
                $http.defaults.headers.common['Auth-Token'] = headers()['auth-token'];
                for (var i in data) {
                    $scope.user[i] = data[i];
                }
                Cookies.setItem('user', JSON.stringify(data), expirationDate, '/', $location.host());
                $location.path('/asset-list');
            }
        }).error(function(data, status, headers, config) {
            if (404 === status) {
                $scope.loginForm.$invalid = true;
            }
        });
        return false;
    }
}
LoginCtrl.$inject = ["$scope", "$location", "$http", "Cookies"];

function AssetListCtrl($scope, $location, AssetsService) {

    $scope.navClass = "register";

    $scope.subHeaderTemplate = "partials/nav-subheader.html";

    $scope.controlsTemplate = "partials/list-controls.html";

    $scope.assets = AssetsService.query();

    $scope.goToDetail = function(id) {
        $location.path('/asset-detail/' + id);
        return false;
    }

    $scope.searchAssets = function() {
        $scope.assets = AssetsService.query({
            "search": $scope.header.search
        });
        return false;
    }
}
AssetListCtrl.$inject = ["$scope", "$location", "AssetsService"];

function GeoAreaCtrl($scope, $http) {

    $scope.subHeaderTemplate = "partials/geo-subheader.html";

    $scope.controlsTemplate = "partials/geo-controls.html";

    var mapOptions = {
        center: new google.maps.LatLng(52.368938, 4.892654),
        zoom: 8,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    var map = new google.maps.Map(angular.element('.google-map').get(0), mapOptions);

    var bounds = new google.maps.LatLngBounds();

    navigator.geolocation.getCurrentPosition(function(position) {

        new google.maps.Marker({
            position: new google.maps.LatLng(position.coords.latitude, position.coords.longitude),
            map: map,
            title:"Current postion",
            animation: google.maps.Animation.DROP,
            icon: "http://maps.google.com/mapfiles/ms/icons/green-dot.png"
        });

        bounds.extend(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));

        map.fitBounds(bounds);
    });

    var timeoutId,
        infoWindow = new google.maps.InfoWindow(),
        mapEvent;

    angular.element('.google-map').on('touchstart', function(e) {
        timeoutId = setTimeout(function() {
            $.ajax({
                url: "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + mapEvent.latLng.lat() + "," + mapEvent.latLng.lng() + "&sensor=false",
                dataType: "json",
                type: "GET",
                success: function(data) {
                    console.log(data);
                    infoWindow.setPosition(mapEvent.latLng);
                    infoWindow.setOptions({
                        maxWidth: 500
                    });
                    infoWindow.setContent(getInfoWindowContent(data.results[0].formatted_address));
                    infoWindow.open(map);
                }
            });
        }, 500);
    });

    angular.element('.google-map').on('touchmove', function(e) {
        clearTimeout(timeoutId);
    });

    angular.element('.google-map').on('touchend', function(e) {
        clearTimeout(timeoutId);
    });

    google.maps.event.addListener(map, 'mousedown', function(e) {
        mapEvent = e;
    });

    function getInfoWindowContent(address) {
        return '<div id="info-window">' +
                '<p class="address">' + address + '</p>' +
                '<p><a class="btn btn-primary">Add location here!</a></p>' +
                '</div>';
    }

}
GeoAreaCtrl.$inject = ['$scope', '$http'];

function AssetDetailCtrl($scope, $routeParams, $location, AssetModels, Locations, AssetsService) {

    $scope.navClass = "register";

    $scope.subHeaderTemplate = "partials/nav-subheader.html";

    $scope.controlsTemplate = "partials/asset-details-controls.html";

    $scope.asset = {};

    if ($routeParams.id) {
        var assetResource = AssetsService.get({id: $routeParams.id}, function() {
            $scope.asset.locationId = assetResource.location.id;
            $scope.asset.assetModelId = assetResource.assetModel.id;
            $scope.asset.oemSerialNumber = assetResource.oemSerialNumber;
            $scope.asset.customSerialNumber = assetResource.customSerialNumber;
            $scope.asset.building = assetResource.specificLocation.building;
            $scope.asset.floor = assetResource.specificLocation.floor;
            $scope.asset.room = assetResource.specificLocation.room;
            $scope.asset.other = assetResource.specificLocation.other;
        });
    }

    $scope.save = function() {
        AssetsService.update({
            id: $routeParams.id
        }, {
            "oemSerialNumber": $scope.asset.oemSerialNumber,
            "customSerialNumber": $scope.asset.customSerialNumber,
            "assetModelId": $scope.asset.assetModelId,
            "locationId": $scope.asset.locationId,
            "building": $scope.asset.building,
            "floor": $scope.asset.floor,
            "room": $scope.asset.room,
            "other": $scope.asset.other,
            "userPartyId": $scope.user.partyId,
            "ownerId": $scope.user.partyId
        }, function() {
            $location.path('/asset-list');
        });
    };

    $scope.duplicate = function() {
        for (var i in $scope.asset) {
            $scope.assetDuplicate[i] = $scope.asset[i];
        }
        $location.path('/new-asset')
    }

    $scope.delete = function() {
        assetResource.$delete({
            "id": $routeParams.id
        }, function() {
            $location.path('/asset-list');
        });
    }

    $scope.models = AssetModels.get();

    $scope.locations = Locations.get();
}
AssetDetailCtrl.$inject = ["$scope", "$routeParams", "$location", "AssetModels", "Locations", "AssetsService"];

function ConnectListCtrl($scope, $location) {

    $scope.navClass = "connect";

    $scope.subHeaderTemplate = "partials/nav-subheader.html";

    $scope.controlsTemplate = "partials/connect-list-controls.html";

    $scope.goToDetail = function() {
        $location.path('/connect-detail');
        return false;
    }
}
ConnectListCtrl.$inject = ["$scope", "$location"];

function ConnectDetailCtrl($scope) {

    $scope.navClass = "connect";

    $scope.subHeaderTemplate = "partials/nav-subheader.html";

    $scope.controlsTemplate = "partials/connect-detail-controls.html";
}
ConnectDetailCtrl.$inject = ["$scope"]

function ActivateListCtrl($scope, $location) {

    $scope.navClass = "activate";

    $scope.subHeaderTemplate = "partials/nav-subheader.html";

    $scope.controlsTemplate = "partials/connect-list-controls.html";

    $scope.goToDetail = function() {
        $location.path('/activate-detail');
        return false;
    }

    $scope.goToActivateDetail = function() {
        $location.path('/activate-connect-detail');
        return false;
    }
}
ActivateListCtrl.$inject = ["$scope", "$location"];

function ActivateDetailCtrl($scope) {

    $scope.navClass = "activate";

    $scope.subHeaderTemplate = "partials/nav-subheader.html";

    $scope.controlsTemplate = "partials/activate-detail-controls.html";

}
ActivateDetailCtrl.$inject = ['$scope'];

function ActivateConnectDetailCtrl() {

}

function HeaderCtrl($scope, $location, Cookies) {
    $scope.logout = function() {
        Cookies.removeItem('token');
        Cookies.removeItem('user');
        $location.path("/");
    }
}
HeaderCtrl.$inject = ["$scope", "$location", "Cookies"];

function NewAssetCtrl($scope, AssetModels, Locations, AssetsService, $location) {

    $scope.navClass = "register";

    $scope.subHeaderTemplate = "partials/nav-subheader.html";

    $scope.controlsTemplate = "partials/asset-details-controls.html";

    $scope.asset = $scope.assetDuplicate;

    $scope.models = AssetModels.get();

    $scope.locations = Locations.get();

    $scope.save = function() {
        AssetsService.save({
            "assetModelId": $scope.asset.assetModelId,
            "locationId": $scope.asset.locationId,
            "userPartyId": $scope.user.partyId,
            "ownerId": $scope.user.partyId,
            "oemSerialNumber": $scope.asset.oemSerialNumber,
            "customSerialNumber": $scope.asset.customSerialNumber,
            "building": $scope.asset.building,
            "floor": $scope.asset.floor,
            "room": $scope.asset.room,
            "other": $scope.asset.other
        }, function() {
            for (var i in $scope.assetDuplicate) {
                delete $scope.assetDuplicate[i];
            }
            $location.path('/asset-list');
        });
    };
}
NewAssetCtrl.$inject = ["$scope", "AssetModels", "Locations", "AssetsService", "$location"];