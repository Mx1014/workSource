//
// EvhListConfigurationsAdminCommandResponse.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfigurationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListConfigurationsAdminCommandResponse
//
@interface EvhListConfigurationsAdminCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhConfigurationDTO*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

