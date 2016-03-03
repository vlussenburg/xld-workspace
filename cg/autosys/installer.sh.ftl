export JAVA_HOME={{JAVA_HOME}}
cd {{TOOL_DIR}}
{{TOOL_DIR}}/asinst.sh validate {{INSTANCE_NAME}} {{AUTOSYS_HOST}} {{AUTOSYS_PORT}} {{COPY_DIR}}/araas_1.0.105.zip {{COPY_DIR}}/env.txt {{ENV_NAME}}
