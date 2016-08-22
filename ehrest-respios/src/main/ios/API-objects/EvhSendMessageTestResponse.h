//
// EvhSendMessageTestResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMessageTestResponse
//
@interface EvhSendMessageTestResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* text;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

