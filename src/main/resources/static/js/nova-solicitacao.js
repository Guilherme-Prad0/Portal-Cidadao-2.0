var isAnonimo = false;

    function toggleAnonimo(cb) {
      isAnonimo = cb.checked;
      document.getElementById('dadosIdentificado').style.display = isAnonimo ? 'none' : 'block';
      document.getElementById('dadosAnonimo').style.display      = isAnonimo ? 'block' : 'none';
      document.getElementById('anonLabel').classList.toggle('checked', isAnonimo);
      updateCounter(document.getElementById('descricao'));
    }

    function updateCounter(ta) {
      var len = ta.value.length, min = isAnonimo ? 100 : 0;
      document.getElementById('descCount').textContent = len + ' caracteres';
      var hint = document.getElementById('descHint');
      if (isAnonimo && len < min) {
        hint.textContent = 'Faltam ' + (min - len) + ' caractere(s) (mínimo anônimo: 100)';
        hint.style.color = 'var(--red)';
      } else if (isAnonimo) {
        hint.textContent = '✓ Mínimo atingido';
        hint.style.color = 'var(--g600)';
      } else {
        hint.textContent = 'Seja o mais detalhado possível';
        hint.style.color = 'var(--text-muted)';
      }
      document.getElementById('descBar').style.width = Math.min(100, Math.round((len/200)*100)) + '%';
    }

    function maskCPF(el) {
      var v=el.value.replace(/\D/g,'').substring(0,11);
      v=v.replace(/(\d{3})(\d)/,'$1.$2').replace(/(\d{3})(\d)/,'$1.$2').replace(/(\d{3})(\d{1,2})$/,'$1-$2');
      el.value=v;
    }
    function maskTel(el) {
      var v=el.value.replace(/\D/g,'').substring(0,11);
      if(v.length>=7) v='('+v.substring(0,2)+') '+v.substring(2,v.length<=10?6:7)+'-'+v.substring(v.length<=10?6:7);
      else if(v.length>2) v='('+v.substring(0,2)+') '+v.substring(2);
      el.value=v;
    }

    function updatePrioPreview(sel) {
      var val = sel.value, p = document.getElementById('prioPreview');
      var map = {
        'SAUDE':             {l:'🚨 Alta prioridade — Prazo: 1 dia',   bg:'var(--red-bg)', b:'#fca5a5', c:'var(--red)'},
        'SEGURANCA_ESCOLAR': {l:'🚨 Alta prioridade — Prazo: 1 dia',   bg:'var(--red-bg)', b:'#fca5a5', c:'var(--red)'},
        'ILUMINACAO':        {l:'⚠️ Média prioridade — Prazo: 3 dias', bg:'var(--amb-bg)', b:'#fde68a', c:'var(--amber)'},
        'BURACO':            {l:'⚠️ Média prioridade — Prazo: 3 dias', bg:'var(--amb-bg)', b:'#fde68a', c:'var(--amber)'},
        'LIMPEZA':           {l:'✅ Baixa prioridade — Prazo: 7 dias', bg:'var(--g50)',    b:'var(--g300)', c:'var(--g700)'}
      };
      if (val && map[val]) {
        var m = map[val];
        p.textContent = m.l;
        p.style.cssText = 'display:block;background:'+m.bg+';border-color:'+m.b+';color:'+m.c+';padding:12px 16px;border-radius:var(--r-md);border:1.5px solid;font-size:.82rem;font-weight:600;margin-top:-4px;margin-bottom:8px;';
      } else { p.style.display = 'none'; }
    }

    function previewImagem(input) {
      if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function(e) {
          var img = document.getElementById('uploadPreview');
          img.src = e.target.result; img.style.display = 'block';
          document.getElementById('uploadPlaceholder').style.display = 'none';
          document.getElementById('uploadNome').textContent = input.files[0].name;
          document.getElementById('uploadNome').style.display = 'block';
        };
        reader.readAsDataURL(input.files[0]);
      }
    }
    function dragOver(e) { e.preventDefault(); document.getElementById('uploadArea').classList.add('drag'); }
    function dragLeave()  { document.getElementById('uploadArea').classList.remove('drag'); }
    function dropFile(e) {
      e.preventDefault(); dragLeave();
      if (e.dataTransfer.files[0]) {
        document.getElementById('imagem').files = e.dataTransfer.files;
        previewImagem(document.getElementById('imagem'));
      }
    }
    function resetForm() {
      isAnonimo = false;
      document.getElementById('dadosIdentificado') && (document.getElementById('dadosIdentificado').style.display = 'block');
      document.getElementById('dadosAnonimo') && (document.getElementById('dadosAnonimo').style.display = 'none');
      document.getElementById('anonLabel') && document.getElementById('anonLabel').classList.remove('checked');
      document.getElementById('prioPreview').style.display = 'none';
      document.getElementById('descCount').textContent = '0 caracteres';
      document.getElementById('descBar').style.width = '0%';
      document.getElementById('uploadPreview').style.display = 'none';
      document.getElementById('uploadPlaceholder').style.display = 'block';
      document.getElementById('uploadNome').style.display = 'none';
    }
    document.getElementById('formSol').addEventListener('submit', function() {
      var btn = document.getElementById('submitBtn');
      btn.innerHTML = 'Enviando...'; btn.disabled = true;
    });