import PropTypes from 'prop-types';
import {requireNativeComponent, View} from 'react-native';

export const HomePageManager = requireNativeComponent(
    'HomePageManager', {
        propTypes: {
            bundle: PropTypes.string,
            ...View.propTypes,
        },
    },
);
