//
// EvhSignupCommand.h
// generated at 2016-03-31 20:15:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSignupCommand
//
@interface EvhSignupCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* type;

@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSNumber* channel_id;

@property(nonatomic, copy) NSNumber* ifExistsThenOverride;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

