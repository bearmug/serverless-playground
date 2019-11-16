'use strict';

module.exports.ping = async event => {
  return {
    statusCode: 200,
    body: JSON.stringify(
      { pong: true }, null, 2
    ),
  };
};
