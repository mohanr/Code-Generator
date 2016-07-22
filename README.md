# Code Transformer in incubation

## An attempt to refactor code from the Antlr ParseTree.

### Option 1

The transformer rules are read from a YAML file. 

### Option 2

Create the ANTLR parsed nodes in Neo4J. The idea is to visualize the code first. I think it is possible to refactor code by manipulating the Neo4J nodes and then using Antlr to fetch the latest snapshot and generate code.

![Neo4J](https://github.com/mohanr/Code-Generator/blob/master/Neo4J%20Nodes.PNG)

I use this plugin.

![Intellij Idea Antlr Plugin](https://github.com/mohanr/Code-Generator/blob/master/Antlr%20IntelliJ%20Idea%20Plugin.png)
