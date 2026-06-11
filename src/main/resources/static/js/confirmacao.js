function fechar() {
    document.getElementById('overlay').style.display = 'none';

    document.querySelector('.main-full').style.filter = 'none';

    document.querySelector('.main-full').style.pointerEvents = 'auto';
}

function copiar() {

    var protocolo =
        document.getElementById('protoBox')
            .textContent
            .trim();

    navigator.clipboard.writeText(protocolo)
        .then(function () {

            var hint =
                document.getElementById('copyHint');

            hint.textContent = '✓ Copiado!';
            hint.style.color = 'var(--g600)';

            setTimeout(function () {
                hint.textContent = 'Clique para copiar';
                hint.style.color = '';
            }, 2000);
        });
}