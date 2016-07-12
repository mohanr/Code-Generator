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

    TransformerRuleConfiguration rules;

    public static final String CONFIG_FILE = "D:\\IdeaProjects\\Antlr\\config.yml";

    public YamlConfiguration() throws FileNotFoundException {

         Constructor constructor = new Constructor(TransformerRuleConfiguration.class);
         TypeDescription ruleDescription = new TypeDescription(TransformerRuleConfiguration.class);
         ruleDescription.putListPropertyType("oldImports", String.class);
         ruleDescription.putListPropertyType("newImports", String.class);
         ruleDescription.putListPropertyType("classIdentifier ", String.class);
         constructor.addTypeDescription( ruleDescription );
         Yaml yaml = new Yaml(constructor);
         rules =
                 (TransformerRuleConfiguration) yaml.loadAs(new FileReader(CONFIG_FILE),TransformerRuleConfiguration.class);
         System.out.println( " Old imports are [ " + rules.getOldImports() + "]");
    }

    public TransformerRuleConfiguration getTransformerRuleConfiguration(){
        return rules;
    }


}
