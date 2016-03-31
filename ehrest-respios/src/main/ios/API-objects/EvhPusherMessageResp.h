//
// EvhPusherMessageResp.h
// generated at 2016-03-31 13:49:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPusherMessageResp
//
@interface EvhPusherMessageResp
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* content;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

