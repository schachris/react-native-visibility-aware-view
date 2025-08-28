import { fixupConfigRules } from "@eslint/compat";
import { FlatCompat } from "@eslint/eslintrc";
import js from "@eslint/js";
import prettier from "eslint-plugin-prettier";
import { defineConfig } from "eslint/config";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: js.configs.recommended,
  allConfig: js.configs.all
});

export default defineConfig([
  {
    extends: fixupConfigRules(compat.extends("@react-native", "prettier")),
    plugins: { prettier },
    rules: {
      "react/react-in-jsx-scope": "off",
      "prettier/prettier": [
        "error",
        {
          experimentalTernaries: false,
          printWidth: 80,
          tabWidth: 2,
          useTabs: false,
          semi: true,
          singleQuote: false,
          quoteProps: "as-needed",
          jsxSingleQuote: false,
          trailingComma: "none",
          bracketSpacing: true,
          bracketSameLine: false,
          jsxBracketSameLine: false,
          arrowParens: "always",
          rangeStart: 0,
          requirePragma: false,
          insertPragma: false,
          proseWrap: "preserve",
          htmlWhitespaceSensitivity: "css",
          endOfLine: "lf",
          embeddedLanguageFormatting: "auto",
          singleAttributePerLine: false,
          vueIndentScriptAndStyle: false,
          plugins: ["prettier-plugin-organize-imports"]
        }
      ]
    }
  },
  {
    ignores: ["node_modules/", "lib/"]
  }
]);
