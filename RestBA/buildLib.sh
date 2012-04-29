echo "Starting Building libs"
rm -rf download/lib
rm -rf download/lib-sources

mvn dependency:copy-dependencies -DoutputDirectory=download/lib
mvn dependency:copy-dependencies -Dclassifier=sources -DoutputDirectory=download/lib-sources -Dmdep.failOnMissingClassifierArtifact=false
mvn jar:jar
cp -f target/restba-1.0.jar download/lib
mvn source:jar
cp -f target/restba-1.0-sources.jar download/lib-sources

echo "End..."
