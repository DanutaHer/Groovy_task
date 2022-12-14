import java.math.RoundingMode

// ---------------------------
//
// HOMEWORK
//
// Use Groovy to write a code under "YOUR CODE GOES BELOW THIS LINE" comment.
// Make sure the code is working in some of the web Groovy consoles, e.g. https://groovyconsole.appspot.com
// Do not over-engineer the solution.
//
// Assume you got some data from a customer and your task is to design a routine that will calculate the average Product price per Group.
//
// The Price of each Product is calculated as:
// Cost * (1 + Margin)
//
// Assume there can be a large number of products.
//
// Plus points:
// - use Groovy closures (wherever it makes sense)
// - make the category look-up performance effective
 
// contains information about [Product, Group, Cost]
def products = [
    ["A", "G1", 20.1],
    ["B", "G2", 98.4],
    ["C", "G1", 49.7],
    ["D", "G3", 35.8],
    ["E", "G3", 105.5],
    ["F", "G1", 55.2],
    ["G", "G1", 12.7],
    ["H", "G3", 88.6],
    ["I", "G1", 5.2],
    ["J", "G2", 72.4]]
 
// contains information about Category classification based on product Cost
// [Category, Cost range from (inclusive), Cost range to (exclusive)]
// i.e. if a Product has Cost between 0 and 25, it belongs to category C1
// ranges are mutually exclusive and the last range has a null as upper limit.
def category = [
    ["C3", 50, 75],
    ["C4", 75, 100],
    ["C2", 25, 50],
    ["C5", 100, null],
    ["C1", 0, 25]]
 
// contains information about margins for each product Category
// [Category, Margin (either percentage or absolute value)]
def margins = [
    "C1" : "20%",
    "C2" : "30%",
    "C3" : "0.4",
    "C4" : "50%",
    "C5" : "0.6"]
 
// ---------------------------
//
// YOUR CODE GOES BELOW THIS LINE
//
// Assign the 'result' variable so the assertion at the end validates
//
// ---------------------------
 
def result = result(products, margins, category)
 
// ---------------------------
//
// IF YOUR CODE WORKS, YOU SHOULD GET "It works!" WRITTEN IN THE CONSOLE
static def marginsAsDecimal(margins){
    margins.each { m ->
        if (m.value.contains("%")) {
            def e = m.value.replaceAll("%", "") as Float
            margins[m.key] = e / 100
        } else {
            margins[m.key] = m.value as Float
        }
    }
}

static findCategory(category,cost) {
    def c = category.find { ct -> cost > ct[1] && cost <= Optional.ofNullable(ct[2]).orElse(Integer.MAX_VALUE)}
    return c[0]
}

static def result(products, margins, category) {
    marginsAsDecimal(margins)
    products.each { p ->
        def prod = p.get(2)
        def mar = findCategory(category, prod)
        def findMar = margins.get(mar)
        def cost = (prod * (1 + findMar))
        p<<cost
    }
    products.groupBy( { it.get(1) })
            .collectEntries { k,v -> [(k): new BigDecimal((v.findAll().sum {
                it.get(3)})/v.size()).setScale(1, RoundingMode.HALF_UP)]
    }
}

// ---------------------------
assert result == [
    "G1" : 37.5,
    "G2" : 124.5,
    "G3" : 116.1
    ] : "It doesn't work"
 
println "It works!"
