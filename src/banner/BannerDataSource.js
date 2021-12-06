export default class BannerDataSource {

    constructor(data, keyExtractor) {
        this._data = data || [];
        this._keyExtractor = keyExtractor;
        this._listeners = [];

        if (!keyExtractor) {
            console.warn(
                'RNBanner/BannerDataSource: missing keyExtractor, it\'s strongly recommended to specify a keyExtractor function ' +
                'in order to use all the features correctly.',
            );

            this._keyExtractor = (item, index) => {
                return JSON.stringify(item) + '_' + index;
            };
        }
    }

    size() {
        return this._data.length;
    }

    get(index) {
        return this._data[index];
    }

    getKey(item, index) {
        return this._keyExtractor(item, index);
    }

    _addListener(listener) {
        this._listeners.push(listener);
    }

    _removeListener(listener) {
        let index = this._listeners.indexOf(listener);
        if (index > -1) {
            this._listeners.splice(index, 1);
        }
    }
}
