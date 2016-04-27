//
// EvhDeleteUserIdentifierCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteUserIdentifierCommand
//
@interface EvhDeleteUserIdentifierCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userIdentifierId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

