//
// EvhListOweFamilysByConditionsCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOweFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOweFamilysByConditionsCommandResponse
//
@interface EvhListOweFamilysByConditionsCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhOweFamilyDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

