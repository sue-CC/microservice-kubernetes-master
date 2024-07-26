# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc
import warnings

from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2
import order_pb2 as order__pb2

GRPC_GENERATED_VERSION = '1.65.1'
GRPC_VERSION = grpc.__version__
EXPECTED_ERROR_RELEASE = '1.66.0'
SCHEDULED_RELEASE_DATE = 'August 6, 2024'
_version_not_supported = False

try:
    from grpc._utilities import first_version_is_lower
    _version_not_supported = first_version_is_lower(GRPC_VERSION, GRPC_GENERATED_VERSION)
except ImportError:
    _version_not_supported = True

if _version_not_supported:
    warnings.warn(
        f'The grpc package installed is at version {GRPC_VERSION},'
        + f' but the generated code in order_pb2_grpc.py depends on'
        + f' grpcio>={GRPC_GENERATED_VERSION}.'
        + f' Please upgrade your grpc module to grpcio>={GRPC_GENERATED_VERSION}'
        + f' or downgrade your generated code using grpcio-tools<={GRPC_VERSION}.'
        + f' This warning will become an error in {EXPECTED_ERROR_RELEASE},'
        + f' scheduled for release on {SCHEDULED_RELEASE_DATE}.',
        RuntimeWarning
    )


class OrderServiceStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.ListOrders = channel.unary_unary(
                '/OrderService/ListOrders',
                request_serializer=google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
                response_deserializer=order__pb2.OrderListResponse.FromString,
                _registered_method=True)
        self.GetOrder = channel.unary_unary(
                '/OrderService/GetOrder',
                request_serializer=order__pb2.OrderId.SerializeToString,
                response_deserializer=order__pb2.OrderResponse.FromString,
                _registered_method=True)
        self.CreateOrder = channel.unary_unary(
                '/OrderService/CreateOrder',
                request_serializer=order__pb2.CreateOrderRequest.SerializeToString,
                response_deserializer=order__pb2.OrderResponse.FromString,
                _registered_method=True)
        self.DeleteOrder = channel.unary_unary(
                '/OrderService/DeleteOrder',
                request_serializer=order__pb2.OrderId.SerializeToString,
                response_deserializer=order__pb2.SuccessResponse.FromString,
                _registered_method=True)
        self.AddLine = channel.unary_unary(
                '/OrderService/AddLine',
                request_serializer=order__pb2.AddLineRequest.SerializeToString,
                response_deserializer=order__pb2.OrderResponse.FromString,
                _registered_method=True)


class OrderServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def ListOrders(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def GetOrder(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def CreateOrder(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def DeleteOrder(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def AddLine(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_OrderServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'ListOrders': grpc.unary_unary_rpc_method_handler(
                    servicer.ListOrders,
                    request_deserializer=google_dot_protobuf_dot_empty__pb2.Empty.FromString,
                    response_serializer=order__pb2.OrderListResponse.SerializeToString,
            ),
            'GetOrder': grpc.unary_unary_rpc_method_handler(
                    servicer.GetOrder,
                    request_deserializer=order__pb2.OrderId.FromString,
                    response_serializer=order__pb2.OrderResponse.SerializeToString,
            ),
            'CreateOrder': grpc.unary_unary_rpc_method_handler(
                    servicer.CreateOrder,
                    request_deserializer=order__pb2.CreateOrderRequest.FromString,
                    response_serializer=order__pb2.OrderResponse.SerializeToString,
            ),
            'DeleteOrder': grpc.unary_unary_rpc_method_handler(
                    servicer.DeleteOrder,
                    request_deserializer=order__pb2.OrderId.FromString,
                    response_serializer=order__pb2.SuccessResponse.SerializeToString,
            ),
            'AddLine': grpc.unary_unary_rpc_method_handler(
                    servicer.AddLine,
                    request_deserializer=order__pb2.AddLineRequest.FromString,
                    response_serializer=order__pb2.OrderResponse.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'OrderService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))
    server.add_registered_method_handlers('OrderService', rpc_method_handlers)


 # This class is part of an EXPERIMENTAL API.
class OrderService(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def ListOrders(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/OrderService/ListOrders',
            google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
            order__pb2.OrderListResponse.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def GetOrder(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/OrderService/GetOrder',
            order__pb2.OrderId.SerializeToString,
            order__pb2.OrderResponse.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def CreateOrder(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/OrderService/CreateOrder',
            order__pb2.CreateOrderRequest.SerializeToString,
            order__pb2.OrderResponse.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def DeleteOrder(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/OrderService/DeleteOrder',
            order__pb2.OrderId.SerializeToString,
            order__pb2.SuccessResponse.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def AddLine(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/OrderService/AddLine',
            order__pb2.AddLineRequest.SerializeToString,
            order__pb2.OrderResponse.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)
