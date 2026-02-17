import os

PROJECT_ROOT = os.getcwd()   # Current directory
OUTPUT_FILE = "all_spring_code.txt"

# Spring-related file extensions
EXTENSIONS = (
    ".java",
    ".kt",
    ".kts",
    ".properties",
    ".yml",
    ".yaml",
    ".xml",
    ".sql",
    ".gradle"
)

# Folders to ignore
EXCLUDE_DIRS = (
    "build",
    ".gradle",
    ".idea",
    "target",
    ".git",
    "node_modules",
    "out"
)

with open(OUTPUT_FILE, "w", encoding="utf-8") as out:
    for root, dirs, files in os.walk(PROJECT_ROOT):
        # Skip noisy folders
        dirs[:] = [d for d in dirs if d not in EXCLUDE_DIRS]

        for file in files:
            if file.endswith(EXTENSIONS):
                file_path = os.path.join(root, file)

                out.write("\n" + "=" * 120 + "\n")
                out.write(f"FILE: {os.path.relpath(file_path, PROJECT_ROOT)}\n")
                out.write("=" * 120 + "\n\n")

                try:
                    with open(file_path, "r", encoding="utf-8") as f:
                        out.write(f.read())
                except Exception as e:
                    out.write(f"[ERROR READING FILE]: {e}\n")

print(f"✅ Extracted into {OUTPUT_FILE}")
