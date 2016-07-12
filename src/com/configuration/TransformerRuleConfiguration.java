package com.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mohan Radhakrishnan on 7/11/2016.
 *
 * The YAML rules are loaded directly into this class.
 * and there should be a way to prevent this from becoming
 * too complicated.
 */
public class TransformerRuleConfiguration {

        private List<String> oldImports;

        private List<String> newImports;

        private String classIdentifier;

        public TransformerRuleConfiguration( ){}

        public List<String> getOldImports() {
            return oldImports;
        }

        public void setOldImports(List<String> oldImports) {
            this.oldImports = oldImports;
        }

        public List<String> getNewImports() {

            return newImports;
        }

        /*
            It is unfortunate that Snakeyaml does not understand Java 8
            @see https://bitbucket.org/asomov/snakeyaml/issues/310/support-bean-properties-of-type
         */
        public Optional<List<String>> getOptionalNewImports() {

            return Optional.<List<String>>ofNullable(newImports);
        }

    public void setNewImports(List<String> newImports) {
            this.newImports = newImports;
        }

        public String getClassIdentifier() {
            return classIdentifier;
        }

        public void setClassIdentifier( String classIdentifier) {
            this.classIdentifier = classIdentifier;
        }

}
