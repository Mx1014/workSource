//
// EvhRegisterLoginCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegisterLoginCommand
//
@interface EvhRegisterLoginCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* borderId;

@property(nonatomic, copy) NSString* loginToken;

@property(nonatomic, copy) NSString* borderSessionId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

