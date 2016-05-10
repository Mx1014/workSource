//
// EvhListConfigurationsAdminCommandResponse.h
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

