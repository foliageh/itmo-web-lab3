const canvas = document.querySelector('#plot__canvas')
const R = 150
const scale = canvas.width / (1.25 * 2 * R)
const ctx = canvas.getContext('2d')

canvas.addEventListener('click', async (e) => {
    const r = getShotR()
    if (!r) {
        shoot([{name: 'error', value: 'R is unset.'}])
        return
    }
    const x = (r * (e.offsetX - canvas.width / 2) / R / scale).toFixed(4)
    const y = (-r * (e.offsetY - canvas.width / 2) / R / scale).toFixed(4)
    shoot([{name: 'x', value: x}, {name: 'y', value: y}, {name: 'r', value: r}])
})

rChanged()
drawPlot()

function rChanged() {
    document.querySelectorAll('input[name="shot_form:r"]').forEach((rCheckbox) => {
        let rList = JSON.parse(sessionStorage.r ?? '[]')
        if (rCheckbox.checked && !rList.includes(rCheckbox.value))
            rList.push(rCheckbox.value)
        else if (!rCheckbox.checked)
            rList = rList.filter((val) => val !== rCheckbox.value)
        sessionStorage.r = JSON.stringify(rList)
    })
    drawPlot()
}

function getShotR() {
    return sessionStorage.r?.length ? parseFloat(JSON.parse(sessionStorage.r).slice(-1)[0]) : null
}

function drawPoint(x, y, r, inArea) {
    x = x * scale / (r / R) + canvas.width / 2
    y = -y * scale / (r / R) + canvas.width / 2
    ctx.beginPath()
    ctx.arc(x, y, 3, 0, 2 * Math.PI, false)
    ctx.fillStyle = inArea ? "yellow" : "grey"
    ctx.fill()
}

function drawPlot() {
    const transformX = (x) => canvas.width / 2 + x * scale
    const transformY = (y) => canvas.height / 2 - y * scale

    const fontSize = 15
    ctx.font = `${fontSize}px monospace`
    ctx.fillStyle = '#34a1ff'
    ctx.strokeStyle = 'black'

    ctx.clearRect(0, 0, canvas.width, canvas.height)

    // rectangle
    ctx.fillRect(transformX(0), transformY(R / 2), R * scale, (R / 2) * scale)

    // triangle
    ctx.beginPath()
    ctx.moveTo(transformX(0), transformY(-R / 2))
    ctx.lineTo(transformX(0), transformY(0))
    ctx.lineTo(transformX(R / 2), transformY(0))
    ctx.closePath()
    ctx.fill()

    // semicircle
    ctx.beginPath()
    ctx.arc(transformX(0), transformY(0), R * scale, Math.PI, -Math.PI / 2, false)
    ctx.lineTo(transformX(0), transformY(0))
    ctx.closePath()
    ctx.fill()

    // coordinate plane
    ctx.beginPath()
    ctx.moveTo(0, canvas.height / 2)
    ctx.lineTo(canvas.width, canvas.height / 2)
    ctx.moveTo(canvas.width - 10, canvas.height / 2 - 5)
    ctx.lineTo(canvas.width, canvas.height / 2)
    ctx.lineTo(canvas.width - 10, canvas.height / 2 + 5)
    ctx.moveTo(canvas.width / 2, canvas.height)
    ctx.lineTo(canvas.width / 2, 0)
    ctx.lineTo(canvas.width / 2 - 5, 10)
    ctx.moveTo(canvas.width / 2, 0)
    ctx.lineTo(canvas.width / 2 + 5, 10)
    ctx.stroke()

    // ticks
    ctx.beginPath()
    for (const [x, y] of [[R / 2, 0], [R, 0], [0, R / 2], [0, R], [-R / 2, 0], [-R, 0], [0, -R / 2], [0, -R]]) {
        ctx.moveTo(transformX(x) - 5 * (x === 0), transformY(y) - 5 * (y === 0))
        ctx.lineTo(transformX(x) + 5 * (x === 0), transformY(y) + 5 * (y === 0))
    }
    ctx.stroke();

    // labels
    const shotR = getShotR()
    let labels = ['-R', '-R/2', 'R/2', 'R']
    if (shotR) labels = [-shotR, -shotR / 2, shotR / 2, shotR].map(r => r.toString())
    ctx.fillStyle = 'black'
    ctx.fillText(labels[0], transformX(-R) - fontSize / 3.6 * labels[0].length, transformY(0) - 8)
    ctx.fillText(labels[1], transformX(-R / 2) - fontSize / 3.6 * labels[1].length, transformY(0) - 8)
    ctx.fillText(labels[2], transformX(R / 2) - fontSize / 3.6 * labels[2].length, transformY(0) - 8)
    ctx.fillText(labels[3], transformX(R) - fontSize / 3.6 * labels[3].length, transformY(0) - 8)
    ctx.fillText('x', transformX(R + 32) - fontSize / 3.6, transformY(0) - 8)
    ctx.fillText(labels[0], transformX(0) + 8, transformY(-R) + fontSize / 3.6)
    ctx.fillText(labels[1], transformX(0) + 8, transformY(-R / 2) + fontSize / 3.6)
    ctx.fillText(labels[2], transformX(0) + 8, transformY(R / 2) + fontSize / 3.6)
    ctx.fillText(labels[3], transformX(0) + 8, transformY(R) + fontSize / 3.6)
    ctx.fillText('y', transformX(0) + 8, transformY(R + 32) + fontSize / 3.6)

    // points
    const shotRows = document.querySelectorAll('#shot_table tbody tr')
    shotRows.forEach(row => {
        const cells = row.querySelectorAll('td')
        if (cells.length !== 4) return
        const [x, y, r] = cells[1].innerText.trim().slice(1, -1).split(';').map(Number);
        if (r === shotR) drawPoint(x, y, r, cells[3].innerText === "In");
    })
}
