//
// EvhGetAllCardDescriptDTO.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetAllCardDescriptDTO
//
@interface EvhGetAllCardDescriptDTO
    : NSObject<EvhJsonSerializable>


// item type NSString*
@property(nonatomic, strong) NSMutableArray* cardDescript;

@property(nonatomic, copy) NSNumber* success;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

