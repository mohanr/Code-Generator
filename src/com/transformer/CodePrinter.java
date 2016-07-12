package com.transformer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;
import test.JavaBaseListener;
import test.JavaParser;

import java.io.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Mohan Radhakrishnan on 6/21/2016.
 */
public class CodePrinter {

    BufferedWriter fout;

    private List<Interval> intervals;

    private CharStream tokenStream;

    public CodePrinter(){
        try {
            fout = new BufferedWriter( new FileWriter("D:\\IdeaProjects\\Antlr\\LoginController.java") );
        } catch (IOException e) {
            System.out.println("IOException");
        }

    }

    public CodePrinter( CharStream tokenStream ){
        this();
        this.tokenStream = tokenStream;
    }


    public void write( List<Interval> intervals) {
        try{
            for (Interval interval : intervals ) {
                fout.write(tokenStream.getText(interval));
                fout.write("\n");
            }
        }catch( IOException ioex ){
            System.out.println( "IOException" );
        }finally{
            try {
                fout.flush();
            } catch (IOException e) {
                System.out.println( "IOException" );
            }
        }
    }

    public void writeText( String text ) {
        try{
                fout.write(text);
                fout.write("\n");
        }catch( IOException ioex ){
            System.out.println( "IOException" );
        }finally{
            try {
                fout.flush();
            } catch (IOException e) {
                System.out.println( "IOException" );
            }
        }
    }

    public void close() throws IOException {
        fout.close();
    }

    public void writeList(List<String> importFilter) {
        try{
            List<String> i =
            importFilter.stream()
                    .collect(Collectors.toList());

            i.forEach((str) -> {
                try {
                    fout.write(str);
                    fout.write("\n");
                } catch (IOException ioex) {
                    System.out.println( "IOException" );
                }
            });
         } finally{
            try {
                fout.flush();
            } catch (IOException e) {
                System.out.println( "IOException" );
            }
        }
    }
}