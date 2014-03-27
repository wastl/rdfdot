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
var rdfdotApp = angular.module('rdfdotApp', ['ui.bootstrap','colorpicker.module']);

rdfdotApp.controller("ConfigurationCtrl", function($scope,$http) {

    $scope.inputs = ['turtle','xml'];
    $scope.outputs = ['png','jpg'];

    $scope.data = "";

    $scope.configuration = {
        'input': 'turtle',
        'output': 'png',
        'base64': 'true',

        uri_shape: 'OVAL',
        uri_fill:  "#ffffff",
        uri_style: 'SOLID',

        bnode_shape: 'POINT',
        bnode_fill:  '#ffffff',
        bnode_style: 'SOLID',

        literal_shape: 'BOX',
        literal_fill:  '#B24CFF',
        literal_style: 'FILLED',

        arrow_shape: 'VEE',
        arrow_style: 'SOLID',
        arrow_color: '#000000'
    };


    $scope.image = null;
    $scope.alerts = [];

    $scope.loader = false;

    $scope.submit = function() {

        $scope.alerts = [];
        $scope.loader = true;

        $http.post("render",$scope.data,{ 'params': $scope.configuration }).
            success(function(img) {
                $scope.loader = false;
                $scope.image = img;
            }).
            error(function(data,status) {
                $scope.loader = false;
                $scope.addAlert(data);
            });
    };

    $scope.addAlert = function(msg) {
        $scope.alerts.push({ type: 'danger', 'msg': msg});
    };

    $scope.closeAlert = function(idx) {
        $scope.alerts.splice(idx, 1);
    };

    // show configuration panel
    $scope.show_configuration = false;


    $scope.node_shapes = ['OVAL','ELLIPSE','EGG','CIRCLE', 'BOX','SQUARE', 'POINT', 'OCTAGON', 'PLAINTEXT'];
    $scope.node_styles = ['SOLID', 'DASHED', 'DOTTED', 'FILLED', 'BOLD'];

    $scope.arrow_shapes = ['NORMAL', 'VEE', 'TEE', 'CURVE', 'DOT', 'DIAMOND'];
    $scope.arrow_styles = ['SOLID', 'DASHED', 'DOTTED', 'BOLD'];



    // color configuration
    $scope.uri_css = {
        'background-color': $scope.configuration.uri_fill
    };

    $scope.$watch('configuration.uri_fill', function() {
        $scope.uri_css['background-color'] = $scope.configuration.uri_fill;
    });


    $scope.bnode_css = {
        'background-color': $scope.configuration.bnode_fill
    };

    $scope.$watch('configuration.bnode_fill', function() {
        $scope.bnode_css['background-color'] = $scope.configuration.bnode_fill;
    });


    $scope.literal_css = {
        'background-color': $scope.configuration.literal_fill
    };

    $scope.$watch('configuration.literal_fill', function() {
        $scope.literal_css['background-color'] = $scope.configuration.literal_fill;
    });


    $scope.edge_css = {
        'background-color': $scope.configuration.arrow_color
    };

    $scope.$watch('configuration.arrow_color', function() {
        $scope.edge_css['background-color'] = $scope.configuration.arrow_color;
    });

    $scope.examples = {
        'FOAF': '@prefix ex: <http://example.com/> .\n@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n\nex:Alice a foaf:Person ;\n    foaf:firstName "Alice" ;\n    foaf:lastName  "Smith" ;\n    foaf:knows ex:Bob .\n        \nex:Bob a foaf:Person ;\n    foaf:firstName "Bob" ;\n    foaf:lastName  "Cook" ;\n    foaf:knows ex:Alice.\n',
        'Students': '@prefix ex: <http://example.com/> .\n@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n\nex:Student rdfs:subClassOf ex:Person.\n\nex:Professor rdfs:subClassOf ex:Person.\n\nex:Algorithms_and_Datastructures a ex:Lecture .\n\nex:Alice a ex:Professor ;\n    rdfs:label "Alice Smith" ;\n    ex:teaches ex:Algorithms_and_Datastructures .\n\nex:Bob a ex:Student ;\n    rdfs:label "Bob Cook" ;\n    ex:attends ex:Algorithms_and_Datastructures .\n'
    };

    $scope.selectExample = function(name) {
        $scope.data = $scope.examples[name];
    };
});

