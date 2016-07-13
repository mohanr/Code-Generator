package com.configuration;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mohan Radhakrishnan on 7/11/2016.
 */
public class YamlConfiguration {

    Yaml yaml = new Yaml();
    Map<String, ArrayList> config;

    static TransformerRuleConfiguration rules;

    public static final String CONFIG_FILE = "D:\\IdeaProjects\\Antlr\\config.yml";

    private YamlConfiguration(){

    }

    public static TransformerRuleConfiguration getDefaultTransformerRuleConfiguration(){
        Constructor constructor = new Constructor(TransformerRuleConfiguration.class);
        TypeDescription ruleDescription = new TypeDescription(TransformerRuleConfiguration.class);
        constructor.addTypeDescription( ruleDescription );
        loadImports( ruleDescription );
        loadClassIdentifier(ruleDescription);
        Yaml yaml = new Yaml(constructor);
        try {
            rules =
                    (TransformerRuleConfiguration) yaml.loadAs(new FileReader(CONFIG_FILE), TransformerRuleConfiguration.class);
        }catch( final FileNotFoundException fnfex ){
            throw new Error(fnfex);
        }
        return rules;
    }

    private static void loadOutputLocation( TypeDescription ruleDescription ) {
        ruleDescription.putListPropertyType("outputDir", String.class);
    }

    private static void loadImports( TypeDescription ruleDescription ){
        ruleDescription.putListPropertyType("oldImports", String.class);
        ruleDescription.putListPropertyType("newImports", String.class);
    }

    private static void loadClassIdentifier( TypeDescription ruleDescription ){
        ruleDescription.putListPropertyType("classIdentifier ", String.class);
    }

}
