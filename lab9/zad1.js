const asyncLib = require("async");

function printAsync(s, cb) {
   var delay = Math.floor((Math.random()*1000)+500);
   setTimeout(function() {
       console.log(s);
       if (cb) cb();
   }, delay);
}

function task(n) {
    return new Promise((resolve, reject) => {
      printAsync(n, function () {
        resolve(n);
      });
    });
  }

  async function tasks() {
    task(1).then((n) => {
    console.log('task', n, 'done');
    return task(2);
    }).then((n) => {
    console.log('task', n, 'done');
    return task(3);
    }).then((n) => {
    console.log('task', n, 'done');
    console.log('done');
    });
   }

  const loop = async m => {
    for (let i = 0; i < m; i++) {
      await tasks();
    }
  };


  //  async function loop(m) {
  //   const tasksToRun = [];
  //   for (let i = 0; i < m; i++) {
  //   tasksToRun.push(async () => await tasks());
  //   }
  //   asyncLib.waterfall(tasksToRun, (err, result) => {
  //   console.log('done');
  //   });
  //  };

   loop(4);
//   loopWaterfall(5);
  

  
//   loop(5);


//   const loopWaterfall = async m => {
//     const tasksToRun = [];
  
//     for (let i = 0; i < m; i++) {
//       tasksToRun.push(async () => await tasks(i));
//     }
  
//     asyncLib.waterfall(tasksToRun, (err, result) => {
//       console.log(chalk.bgGreen.black(`done ${result}`));
//     });
//   };
  
  
  
//   console.log(chalk.bgBlueBright.black("Normal loop"));
//   loop(4).then(() => {
//     console.log(chalk.bgBlueBright.black("Waterfall loop"));
//     loopWaterfall(4);
//   })
  

// printAsync("1");
// printAsync("2");
// printAsync("3");

// console.log('done!');