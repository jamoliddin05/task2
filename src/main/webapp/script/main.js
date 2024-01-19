
function getIndex(list, id) {
    for (let i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}

let productApi = Vue.resource('/service/products/{id}');

Vue.component('product-form', {
    props: ['products', 'productAttr'],
    data: function () {
        return {
            name: '',
            price: '',
            id: ''
        }
    },
    watch: {
        productAttr: function(newVal, oldVal) {
            this.id = newVal.id;
            this.name = newVal.name;
            this.price = newVal.price;
        }
    },
    template: '<div>' +
        '<input type="text" placeholder="Product name" v-model="name"/><br>' +
        '<input type="text" placeholder="Product price" v-model="price"/><br>' +
        '<input type="button" value="Save" @click="save"/>' +
        '</div>',
    methods: {
        save: function () {
            let product = {name: this.name, price: this.price};

            if (this.id) {
                productApi.update({id: this.id}, product).then(result =>
                    result.json().then(data => {
                        let index = getIndex(this.products, data.id);
                        this.products.splice(index, 1, data);
                        this.name = ''
                        this.price = ''
                        this.id = ''
                    })
                )
            } else {
                productApi.save({}, product).then(
                    result => result.json().then(
                        data => {
                            this.products.push(data);
                            this.name = '';
                            this.price = ''
                        })
                )
            }
        }
    }
})

Vue.component('product-row', {
    props: ['product', 'editMethod', 'products'],
    template: '<div>' +
        '<i>({{ product.name }})</i> {{ product.price }}' +
        '<span style="position: absolute; right: 0;">' +
            '<input type="button" value="Edit" @click="edit" />' +
            '<input type="button" value="X" @click="del" />' +
        '</span>' +
        '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.product)
        },
        del: function () {
            productApi.remove({id: this.product.id}).then(result => {
                if (result.ok) {
                    this.products.splice(this.products.indexOf(this.product), 1)
                }
            })
        }
    }
});

Vue.component('product-list', {
    props: ['products'],
    data: function () {
        return {
            product: null
        }
    },
    template: '<div style="position: relative; width: 300px;">' +
        '<product-form :products="products" :productAttr="product" />' +
        '<product-row v-for="product in products" :key="product.id" :product="product" ' +
        ' :editMethod="editMethod" :products="products"/>' +
        '</div>',
    created: function () {
        productApi.get().then(result =>
            result.json().then(data =>
                data.forEach(product => this.products.push(product)))
        )
    },
    methods: {
        editMethod: function (product) {
            this.product = product
        }
    }
});

let app = new Vue({
    el: "#app",
    template: '<product-list :products="products"/>',
    data: {
        products: []
    }
});