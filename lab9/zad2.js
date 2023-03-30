const walkdir = require("walkdir");
const fs = require("fs");
const asyncLib = require("async");

let count = 0;
const countLines = (file, cb) => {
  let fileCount = 0;
  fs.createReadStream(file)
    .on("data", function (chunk) {
      const add = chunk
      .toString("utf8")
      .split(/\r\n|[\n\r\u0085\u2028\u2029]/g)
      .length - 1;
      count += add;
      fileCount += add;
    })
    .on("end", function () {
      // console.log(file, fileCount);
      cb();
    })
    .on("error", function (err) {
      // console.error(err);
      cb();
    });
};

const runSync = async () => {
  const paths = walkdir.sync("./PAM08");
  const tasks = paths.map(path => cb => countLines(path, cb));
  var start = new Date().getTime();

  asyncLib.waterfall(tasks, () => {
    console.log(count);
    var end = new Date().getTime();
    console.log(`${end - start}`);
  });
};

const runAsync = async () => {
  const paths = walkdir.sync("./PAM08");
  const tasks = paths.map(path => cb => countLines(path, cb));
  var start = new Date().getTime();

  asyncLib.parallel(tasks, () => {
    console.log(count);
    var end = new Date().getTime();
    console.log(`${end - start}`);
  });
};

// runSync();
runAsync();
