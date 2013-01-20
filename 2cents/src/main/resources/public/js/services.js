twocents.factory("AssetsService", ["$resource", "$rootScope", function($resource, $rootScope) {
    return $resource(
        $rootScope.serviceUrls.getAssetsUrl() + "/:id",
        {
            id: "@id"
        },
        {
            update: {
                method: "PUT"
            }
        }
    );
}]);

twocents.factory("AssetModels", ["$resource", "$rootScope", function($resource, $rootScope) {
    return $resource(
        $rootScope.serviceUrls.getAssetModelsUrl(),
        {},
        {
            get: {
                method: "GET",
                isArray: true
            }
        }
    );
}]);

twocents.factory("Locations", ["$resource", "$rootScope", function($resource, $rootScope) {
    return $resource(
        $rootScope.serviceUrls.getLocationsUrl(),
        {},
        {
            get: {
                method: "GET",
                isArray: true
            }
        }
    );
}]);

twocents.factory("AuthResponseInterceptor", ["$q", "$location", "Cookies", function($q, $location, Cookies) {
    return function(promise) {
        var redirected = false;
        return promise.then(function(response) {
            // success
            return response;
        }, function(response) {
            // error
            if (response.status === 401) {
                Cookies.removeItem('token');
                // if multiple requests are pending then redirect only once
                if (!redirected) {
                    redirected = true;
                    $location.path("/");
                }
            }
            return $q.reject(response);
        });
    }
}]);

twocents.factory("Cookies", function() {

    // taken from https://developer.mozilla.org/en-US/docs/DOM/document.cookie
    return {
        getItem: function (sKey) {
            if (!sKey || !this.hasItem(sKey)) { return null; }
            return unescape(document.cookie.replace(new RegExp("(?:^|.*;\\s*)" + escape(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*((?:[^;](?!;))*[^;]?).*"), "$1"));
        },
        setItem: function (sKey, sValue, vEnd, sPath, sDomain, bSecure) {
            if (!sKey || /^(?:expires|max\-age|path|domain|secure)$/i.test(sKey)) { return; }
            var sExpires = "";
            if (vEnd) {
                switch (vEnd.constructor) {
                    case Number:
                        sExpires = vEnd === Infinity ? "; expires=Tue, 19 Jan 2038 03:14:07 GMT" : "; max-age=" + vEnd;
                        break;
                    case String:
                        sExpires = "; expires=" + vEnd;
                        break;
                    case Date:
                        sExpires = "; expires=" + vEnd.toGMTString();
                        break;
                }
            }
            document.cookie = escape(sKey) + "=" + escape(sValue) + sExpires + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "") + (bSecure ? "; secure" : "");
        },
        removeItem: function (sKey, sPath) {
            if (!sKey || !this.hasItem(sKey)) { return; }
            document.cookie = escape(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT" + (sPath ? "; path=" + sPath : "");
        },
        hasItem: function (sKey) {
            return (new RegExp("(?:^|;\\s*)" + escape(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
        },
        keys: function () {
            var aKeys = document.cookie.replace(/((?:^|\s*;)[^\=]+)(?=;|$)|^\s*|\s*(?:\=[^;]*)?(?:\1|$)/g, "").split(/\s*(?:\=[^;]*)?;\s*/);
            for (var nIdx = 0; nIdx < aKeys.length; nIdx++) { aKeys[nIdx] = unescape(aKeys[nIdx]); }
            return aKeys;
        }
    };
});