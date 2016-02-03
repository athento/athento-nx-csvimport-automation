# athento-nx-csvimport-automation

## Synopsis

This Nuxeo plugin implements an operation of automation endpoint REST to launch CSV import.

## Motivation

The CSV import/export plugin of Nuxeo is used into UI by Seam component. We need an operation into automation endpoint to launch import CSV.

## Installation

You just have to compile the pom.xml using Maven and deploy the plugin in 
```{r, engine='bash', count_lines}
cd athento-nx-csvimport-automation
mvn clean install
cp target/athento-nx-csvimport-automation.jar $NUXEO_HOME/nxserver/plugins
```
And then, restart your nuxeo server and enjoy.
