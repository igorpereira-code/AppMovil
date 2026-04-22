#!/bin/bash

# ─── CONFIGURACIÓN ────────────────────────────────────────────────────────────
API_KEY="y4x5mbP8qwj9iBID3x0TmZW15a7Eg4iy"
BASE_URL="https://localise.biz/api/export/locale"

# Ruta adaptada a tu estructura KMP (módulo composeApp)
OUTPUT="composeApp/src/commonMain/composeResources"
# ──────────────────────────────────────────────────────────────────────────────

echo "🔄 Sincronizando traducciones desde Localise.biz..."

# Crear carpetas si no existen
mkdir -p "$OUTPUT/values"
mkdir -p "$OUTPUT/values-en"

# ── Español (idioma por defecto → va en values/) ──────────────────────────────
echo "  📥 Descargando español (es)..."
curl -s "$BASE_URL/es.xml?format=android" \
  -u "$API_KEY:" \
  -o "$OUTPUT/values/strings.xml"

if [ $? -eq 0 ]; then
  # Limpiar namespace XLIFF que Loco agrega y KMP no reconoce
  sed -i 's/ xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2"//g' "$OUTPUT/values/strings.xml"
  echo "  ✅ values/strings.xml actualizado"
else
  echo "  ❌ Error al descargar español"
fi

# ── Inglés ────────────────────────────────────────────────────────────────────
echo "  📥 Descargando inglés (en)..."
curl -s "$BASE_URL/en.xml?format=android" \
  -u "$API_KEY:" \
  -o "$OUTPUT/values-en/strings.xml"

if [ $? -eq 0 ]; then
  # Limpiar namespace XLIFF que Loco agrega y KMP no reconoce
  sed -i 's/ xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2"//g' "$OUTPUT/values-en/strings.xml"
  echo "  ✅ values-en/strings.xml actualizado"
else
  echo "  ❌ Error al descargar inglés"
fi

echo ""
echo "✨ ¡Listo! Haz Sync del proyecto en Android Studio para ver los cambios."
