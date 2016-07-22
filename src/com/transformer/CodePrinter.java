package com.transformer;

import com.configuration.TransformerRuleConfiguration;
import com.configuration.YamlConfiguration;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;
import com.antlr.framework.JavaBaseListener;
import com.antlr.framework.JavaParser;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Mohan Radhakrishnan on 6/21/2016.
 */
public class CodePrinter {

    private String classIdentifier = "DEFAULT";

    private BufferedWriter fout;

    private List<Interval> intervals;

    private CharStream tokenStream;

    private Optional<String> outputClassIdentifier;

    private TransformerRuleConfiguration rules = YamlConfiguration.getDefaultTransformerRuleConfiguration();


    public CodePrinter(CharStream inputStream, Optional<String> outputClassIdentifier){
        this.tokenStream = inputStream;
        try {
            rules.getOptionalClassIdentifier().ifPresent( a -> {this.classIdentifier = a;});
            if (rules.getOptionalOutputDir().isPresent()){
                fout = new BufferedWriter( new FileWriter( rules.getOptionalOutputDir().get() +
                                                            ( outputClassIdentifier.isPresent() ?
                                                                outputClassIdentifier.get() : classIdentifier ) + ".java") );
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
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