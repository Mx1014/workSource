//
// EvhSendPmPayMessageToAllOweFamiliesCommand.h
// generated at 2016-04-22 13:56:45 
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

