//
// EvhNewQRCodeCommand.h
// generated at 2016-04-19 13:40:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewQRCodeCommand
//
@interface EvhNewQRCodeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* logoUri;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* expireSeconds;

@property(nonatomic, copy) NSNumber* actionType;

@property(nonatomic, copy) NSString* actionData;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

