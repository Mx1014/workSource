//
// EvhListOweFamilysByConditionsCommandResponse.h
// generated at 2016-04-08 20:09:23 
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

