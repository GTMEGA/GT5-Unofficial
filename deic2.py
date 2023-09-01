import os, sys
import re


def main():
    spider()


def spider():
    rx = re.compile(r"import\s*(ic2.*);\n")
    result_files = []
    for subdir, dirs, files in os.walk("."):
        for file in files:
            if subdir.startswith(".\\src\\main\\java\\gregtech") and file.endswith(".java"):
                result_files.append(os.path.abspath(os.path.join(subdir, file)))
    for file in result_files:
        with open(file, "r") as f:
            print(file)
            lines = f.readlines()
            with open(file, "w") as f2:
                for l in lines:
                    if rx.match(l):
                        print(repr(l))
                    else:
                        f2.write(l)


if __name__ == '__main__':
    main()
