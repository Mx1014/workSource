//
// EvhListOweFamilysByConditionsCommandResponse.h
// generated at 2016-03-25 09:26:41 
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

