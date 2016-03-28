//
// EvhSendPmPayMessageToAllOweFamiliesCommand.h
// generated at 2016-03-25 19:05:18 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendPmPayMessageToAllOweFamiliesCommand
//
@interface EvhSendPmPayMessageToAllOweFamiliesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

