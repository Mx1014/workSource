//
// EvhListOweFamilysByConditionsCommandResponse.h
// generated at 2016-04-18 14:48:52 
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

