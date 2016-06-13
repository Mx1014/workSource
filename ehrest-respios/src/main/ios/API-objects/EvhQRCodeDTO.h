//
// EvhQRCodeDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQRCodeDTO
//
@interface EvhQRCodeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* qrid;

@property(nonatomic, copy) NSString* logoUri;

@property(nonatomic, copy) NSString* logoUrl;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* expireTime;

@property(nonatomic, copy) NSNumber* actionType;

@property(nonatomic, copy) NSString* actionData;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* url;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

