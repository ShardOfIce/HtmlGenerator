Overview

This program is designed to draw rectangles in an HTML file using data from a JSON file, 
with an option to automatically open the generated file in Google Chrome. 
This feature can be useful for visualizing rectangles stored in JSON format.

Usage

To use the program, execute the following command in the terminal in the root project folder:
java -jar HG.jar convert example_1.json

To convert a JSON file into HTML and automatically open it in Chrome, use the --open key:
java -jar HG.jar convert example_1.json --open