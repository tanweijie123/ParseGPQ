package project.files;

import com.google.gson.*;
import project.model.rule.Rule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Scanner;

class ConfigJsonAdapter {

    static boolean saveSettings(Config config) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Rule.class, new InterfaceAdapter<Rule>());
        Gson gson = builder.create();

        String exportString = gson.toJson(config);
        return writeToFile(exportString);
    }

    static boolean writeToFile(String exportString) {
        try {
            File file = new File("config.json");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(exportString);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            System.err.println("Unable to write to config.json");
            return false;
        }
    }

    static Optional<Config> loadSettings() {
        File file = new File("config.json");
        if (!file.exists()) {
            writeToFile("{\n" +
                    "  \"googleSheetLink\": \"\",\n" +
                    "  \"downloadedExcelPath\": \"data/forms.xlsx\",\n" +
                    "  \"exportExcelPath\": \"data/output.xlsx\"\n" +
                    "}");
        }

        try {
            Scanner readFile = new Scanner(new File("config.json"));
            String importString = readFile.useDelimiter("\\Z").next();
            readFile.close();

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            builder.registerTypeAdapter(Rule.class, new InterfaceAdapter<Rule>());
            Gson gson = builder.create();

            return Optional.of(gson.fromJson(importString, Config.class));
        } catch (Exception e) {
            return Optional.empty(); //this should not happen.
        }
    }

    /**
     * This class file was copied from https://stackoverflow.com/questions/16000163/using-gson-and-abstract-classes
     */
    private static class InterfaceAdapter<T>
            implements JsonSerializer<T>, JsonDeserializer<T> {

        @Override
        public final JsonElement serialize(final T object, final Type interfaceType, final JsonSerializationContext context)
        {
            final JsonObject member = new JsonObject();

            member.addProperty("type", object.getClass().getName());

            member.add("data", context.serialize(object));

            return member;
        }

        @Override
        public final T deserialize(final JsonElement elem, final Type interfaceType, final JsonDeserializationContext context)
                throws JsonParseException
        {
            final JsonObject member = (JsonObject) elem;
            final JsonElement typeString = get(member, "type");
            final JsonElement data = get(member, "data");
            final Type actualType = typeForName(typeString);

            return context.deserialize(data, actualType);
        }

        private Type typeForName(final JsonElement typeElem)
        {
            try
            {
                return Class.forName(typeElem.getAsString());
            }
            catch (ClassNotFoundException e)
            {
                throw new JsonParseException(e);
            }
        }

        private JsonElement get(final JsonObject wrapper, final String memberName)
        {
            final JsonElement elem = wrapper.get(memberName);

            if (elem == null)
            {
                throw new JsonParseException(
                        "no '" + memberName + "' member found in json file.");
            }
            return elem;
        }
    }
}
