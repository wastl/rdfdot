/*
 * Copyright 2014 Sebastian Schaffert (wastl@wastl.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by wastl on 24.03.14.
 */
var rdfdotApp = angular.module('rdfdotApp', ['ui.bootstrap']);

rdfdotApp.controller("ConfigurationCtrl", function($scope,$http) {

    $scope.inputs = ['turtle','xml'];
    $scope.outputs = ['png','jpg'];

    $scope.data = "";

    $scope.configuration = {
        'input': 'turtle',
        'output': 'png'
    };

    $scope.image = null;

    $scope.submit = function() {

        $http.post("render?base64=true&input="+$scope.configuration.input,$scope.data).success(function(img) {
            console.log(img);
            $scope.image = img;
        });
    }
});

