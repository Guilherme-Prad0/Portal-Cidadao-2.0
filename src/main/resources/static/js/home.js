var cur = 0;
var total = 3;

var track = document.getElementById('track');
var dotsEl = document.getElementById('dots');

for (var i = 0; i < total; i++) {
    var d = document.createElement('button');

    d.className = 'dot' + (i === 0 ? ' active' : '');

    d.onclick = (function(idx) {
        return function() {
            goTo(idx);
        };
    })(i);

    dotsEl.appendChild(d);
}

function goTo(n) {
    cur = (n + total) % total;

    track.style.transform =
        'translateX(-' + (cur * 100) + '%)';

    document
        .querySelectorAll('.dot')
        .forEach(function(d, i) {
            d.classList.toggle('active', i === cur);
        });
}

function move(dir) {
    goTo(cur + dir);
}

setInterval(function () {
    move(1);
}, 5000);