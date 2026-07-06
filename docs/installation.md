# Installation

## Requirements

- Java 25+
- Gradle
- Paper server 26.1.2+

## Build

```bash
gradle build
```

The plugin JAR will be created in:

```text
build/libs/
```

## Install on a server

1. Stop your Minecraft server.
2. Copy the plugin JAR into `plugins/`.
3. Start the server.
4. Edit the generated config file in `plugins/StarterPlugin/config.yml`.
5. Run `/starter reload` or restart the server.
