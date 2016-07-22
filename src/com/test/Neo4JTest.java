package com.test;

import org.antlr.v4.misc.Graph;
import org.antlr.v4.runtime.tree.Tree;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.collection.Iterators;

import java.io.File;
import java.util.*;

/**
 * Created by Mohan Radhakrishnan on 7/15/2016.
 */
public class Neo4JTest {

     static GraphDatabaseService db;

    public static void main(String[] argv){
         File file = new File("D:\\IdeaProjects\\Antlr\\neo4j");
         db = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( file ).
                setConfig( GraphDatabaseSettings.node_keys_indexable, "type,name" ).
                setConfig( GraphDatabaseSettings.relationship_keys_indexable, "IMPLEMENTS" ).
                setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
                setConfig( GraphDatabaseSettings.relationship_auto_indexing, "true" ).
                newGraphDatabase();

         Map<Long, Tree> id2Tree = new HashMap<Long, Tree>();

         Stack<Graph.Node> clazzes = new Stack<Graph.Node>();

//        List<Tree> l = runCypher("start ret=node:node_auto_index(type = 'class') return ret");
//        for (Tree t : l) {
//            System.out.println("Compilation unit found: " + t.toStringTree());
//        }
    }

    private enum Rels implements RelationshipType {
        IMPLEMENTS
    }

    public static final List<Tree> runCypher(String stmt) {

        Map<Long, Tree> id2Tree = new HashMap<Long, Tree>();

        Result result = db.execute(stmt);
        Iterator<Node> ci = result.columnAs("ret");
        List<Tree> ret = new ArrayList<Tree>();
        for (Node node : Iterators.asIterable(ci)) {
            ret.add(id2Tree.get(node.getId()));
        }

        return ret;
    }

}
