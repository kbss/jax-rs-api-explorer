/**
 * created by Serhii Kryvtsov
 */
jaxRsApiExplorer.controller('HomeController',
    ['$scope', '$rootScope', '$log', '$location', '$window', '$http', function ($scope, $rootScope, $log, $location, $window, $http) {

        $rootScope.isLoading = true;
        $scope.classes = [];
        $scope.class = {};
        $scope.method = {};
        $scope.showClass = false;
        $scope.path = "";
        $scope.params = {
            pathParams: [],
            queryParams: [],
            headerParams: []
        }


        var API = {
            getApi: function () {
                $http.get("services/analyze")
                    .success(function (data, status, header, config) {
                        $rootScope.isLoading = false;
                        $scope.classes = data;
                    })
                    .error(function (data, status, headers, config) {
                        $rootScope.isLoading = false;
                    });
            }
        }
        API.getApi();

        $scope.selectClass = function (index) {
            $scope.class = $scope.classes[index];
            $scope.showClass = true;
        }
        $scope.selectMethod = function (index) {
            $log.debug("Show method details " + index);
            $scope.method = $scope.class.methods[index];

            $log.debug($scope.method);
            $scope.showMethod = true;

            $scope.path = $scope.method.path;
        }
        $scope.paramChanged = function () {
            $log.debug("=" + $scope.params.pathParams.length);
            $scope.path = $scope.method.path;
            for (var i = 0; i < $scope.params.pathParams.length; i++) {
                $log.debug("=" + $scope.params.pathParams[i]);
                var token = "${" + (i + 1) + "}";
                var value = $scope.params.pathParams[i];
                if (!value) {
                    value = "";
                }
                $scope.path = $scope.method.path.replace(token, value);
                $log.debug($scope.path);
            }
            if ($scope.params.queryParams.length < 1) {
                return;
            }
            if (!$scope.path.endsWith("?")) {
                $scope.path = $scope.path + "?";
            }
            for (var i = 0; i < $scope.params.queryParams.length; i++) {
                if ($scope.params.queryParams[i]) {
                    $scope.path = $scope.path + $scope.method.queryParams[i].name + "=" + $scope.params.queryParams[i] + "&";
                }
            }
            $scope.path = $scope.path.substring(0, $scope.path.length - 1);
        }
    }]
)
;