//
// EvhGetAllCardDescriptDTO.h
// generated at 2016-03-31 13:49:14 
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

