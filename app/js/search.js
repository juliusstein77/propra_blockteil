function loadSearchResult(args) {
    //console.log("success ", args)
    const fd = new FormData();
    fd.append('query', args);
    fetch('http://bioclient1.bio.ifi.lmu.de/~hummelj/cgi-bin/api/search.py?mode=query', {
        method: 'POST',
        body: fd
    })
    .then(response => response.json())
    .then(data => {
        const outputTag = document.getElementById('searchResultContainer');
        outputTag.className = 'border border-gray-300 rounded-lg px-4 py-2 flex flex-col justify-start items-start w-full gap-2';
        outputTag.innerHTML = '';
        console.log(data);
        Object.values(data).forEach(value => {
            for (const [key, val] of Object.entries(value)) {
                
            }
        });
    })
    .finally(() => {
    
    
    });
}

function to_json(data){
    var json = {};
    var data = data.split('>');
    for (let i = 1; i < data.length; i++) {
        var sequence = data[i].split('\n');
        const id = sequence[0].trim();
        let pred = {};
        for (let j = 1; j < sequence.length-1; j++) {
            const bin = sequence[j].split(' ');
            pred[bin[0]] = bin[1];
        }
        json[id] = pred;

    }
    return json;
}

